package com.yunting.Screenshot.services;

import com.yunting.Screenshot.dto.img.ImgContainer;
import com.yunting.Screenshot.dto.img.ImgShow;
import com.yunting.Screenshot.dto.img.ImgVo;
import com.yunting.Screenshot.entity.Application;
import com.yunting.Screenshot.entity.ImageMapping;
import com.yunting.Screenshot.entity.Imgorder;
import com.yunting.Screenshot.mapper.ApplicationMapper;
import com.yunting.Screenshot.mapper.ImageMappingMapper;
import com.yunting.Screenshot.mapper.ImgorderMapper;
import com.yunting.Screenshot.mapper.PlayerMapper;
import com.yunting.Screenshot.utils.MinIoUtils;
import com.yunting.common.Dto.PlayerDTO;
import com.yunting.common.exception.AppException;
import com.yunting.common.results.ResponseEnum;
import com.yunting.common.results.ResultMessage;
import com.yunting.common.utils.SpringRollBackUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static com.yunting.common.utils.FS.IMG_TYPE;

@Slf4j
@Service("PlayerService")
@Data
public class PlayerImpl implements PlayerService {

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "ApplicationMapper")
    private ApplicationMapper applicationMapper;


    @Resource(name = "MinIoUtils")
    MinIoUtils minIoUtils;

    @Resource(name = "ImageMappingMapper")
    ImageMappingMapper imageMappingMapper;

    @Resource(name = "ImgorderMapper")
    ImgorderMapper imgorderMapper;



    // 获取玩家未上传的图片
    public List getThisPlayerPreUploadImg(PlayerDTO playerDTO, String imgType) {
        Long playerId = playerDTO.getPlayerId();
        List<ImgShow> imgShows = imgorderMapper.selectPlayerOrderNoURl(playerId, imgType);
        return imgShows;
    }


    @Transactional(rollbackFor = Exception.class)
    public ResultMessage uploadImgByPlayerAndAddOrder(PlayerDTO playerDTO, String androidID, List<MultipartFile> files) throws IOException {
        Long playerId = playerDTO.getPlayerId();
        Long gameId = playerDTO.getGameId();
        String nickname = playerMapper.selectWxNicknameByPlayerId(playerId, gameId);
        String withdrawPercentage_str = applicationMapper.getWithdrawPercentage(gameId);
        BigDecimal withdrawPercentage = new BigDecimal(withdrawPercentage_str);

        //创建一笔订单
        Imgorder imgorder = Imgorder.builder()
                .orderPlayerId(playerDTO.getPlayerId())
                .androidID(androidID)
                .wxname(nickname)
                .withdrawPercentage(withdrawPercentage)
                .appid(gameId)
                .build();
        imgorderMapper.insertOneOrder(imgorder);
        Long orderId = imgorder.getOrderId();


        //指定该上传的图片,都属于该笔订单
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getSize() > 1024 * 1024 * 5) {  // 超过5M
                log.error("单个文件过大，请重新上传", new Throwable(new AppException(ResponseEnum.FILE_FULL_HANDLE_ERROR)));
                throw new AppException(ResponseEnum.FILE_FULL_HANDLE_ERROR);
            }

            String originName = files.get(i).getOriginalFilename();                        //传过来的图片名
            String originHash = minIoUtils.calculateSHA256(files.get(i).getInputStream()); //传过来的图片的hash

            ImageMapping imgInfo = imgorderMapper.selectImgByImginfo(originHash, originName);

//解析该图片的hash,通过文件名+hash去找 如果在数据库中存在,就存,不存在 ,那就是图片造假
            if (imgInfo == null) {
                log.error("图片不存在|图片hash不匹配|图片未找到,文件名称:" + originName, new AppException(ResponseEnum.IMG_NOT_MATCH_RESPONSE));
                throw new AppException(ResponseEnum.IMG_NOT_MATCH_RESPONSE);
            }

            Long imgID = imgInfo.getImgId(); //要上传的图片的id,用以修改数据库img_url

            String obj = imgInfo.getDirectory() + "/" + originName + IMG_TYPE; //拼接的路径 | 文件上传的路径

            try {
                String url = minIoUtils.uploadFile(files.get(i), gameId + "", obj);

                imageMappingMapper.updateByPrimaryKey(imgID, url, orderId);  //指定该图片属于哪笔订单,以及,绑定访问的url
            } catch (IOException e) {
                log.error("上传文件失败" + e.getMessage());
                return new ResultMessage(ResponseEnum.MINIO_ERROR_RESPONSE, null);
            }

        }


        log.info("文件上传成功");
        return new ResultMessage(ResponseEnum.SUCCESS, null);
    }


    // 定义一个格式化器，格式为 "yyyy-MM-dd HH:mm:ss"
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
    private static LocalDateTime endTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

    @Transactional(rollbackFor = Exception.class)
    public void preUploadFileNameAndHashVal(PlayerDTO playerDTO, ImgContainer imgContainer) throws IOException {
        String imgType = imgContainer.getItp();
        List<ImgVo> imgVo = imgContainer.getImgVos();
        String imgMoney = imgContainer.getImgMoney();
        String imgBusiness = imgContainer.getImgBusiness();
        String imgTrans = imgContainer.getImgTrans();
        String imgBusinessId = imgContainer.getImgBusinessId();
        LocalDateTime imgPayTime = imgContainer.getImgPayTime();

        Long gameId = playerDTO.getGameId();
        LocalDateTime now = LocalDateTime.now();
        // 使用 formatter 格式化当前时间
        String s = now.format(formatter);

        //构建上传文件的文件路径
        String replaceDate = s.replace("-", "/");
        replaceDate = replaceDate.trim();//替换+去空    生成随机文件名 ,用以覆盖原始文件名

        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        uuid = uuid.trim();
        String upload_dir = uuid + replaceDate; //要上传的文件目录 | 以日期来区分  (图片组是随机的)

        try {
            for (ImgVo vo : imgVo) {

                if(imgType.equals("t")){
                    //插入订单截图之前,先看有没有该笔订单 ,有的话就直接报错了
                    int t = imageMappingMapper.selectImgByImgTypeAndTransidAndUploadTime("t", imgTrans, now, startTime, endTime);
                    if(t>0){
                        log.error("订单号重复", new AppException(ResponseEnum.IMG_TRANS_REPEAT_RESPONSE));
                        throw  new AppException(ResponseEnum.IMG_TRANS_REPEAT_RESPONSE);
                    }

                    //如果是订单截图就要插入图片的订单信息
                    ImageMapping imageMapping = ImageMapping.builder()
                            .directory(upload_dir)
                            .fileName(vo.getImgName())
                            .fileHash(vo.getImgHash())
                            .imgType(imgType)
                            .imgMoney(imgMoney)//充值金额
                            .imgBusiness(imgBusiness)//商户名
                            .imgTrans(imgTrans)//交易单号
                            .imgBusinessId(imgBusinessId)//商户单号
                            .imgPayTime(imgPayTime)//充值时间

                            .playerId(playerDTO.getPlayerId())
                            .uploadTime(now)  //图片上传时间和订单生成时间差不了24小时
                            .build();
                    imageMappingMapper.insertWithTrans(imageMapping);
                }else {
                    ImageMapping imageMapping = ImageMapping.builder()
                            .directory(upload_dir)
                            .fileName(vo.getImgName())
                            .fileHash(vo.getImgHash())
                            .imgType(imgType)
                            .playerId(playerDTO.getPlayerId())
                            .uploadTime(now)
                            .build();
                    imageMappingMapper.insert(imageMapping);
                }

                minIoUtils.detectBucket(gameId + "");//检测是否存在bucket,不存在就创建
                minIoUtils.createDirectory(gameId + "", upload_dir); //创建图片组 相关目录
            }

        } catch (AppException ae) {
            log.error(ae.getCode(), ae.getMessage(), new AppException(ae.getCode(), ae.getMessage()));
            SpringRollBackUtil.rollBack();
            throw new AppException(ae.getCode(), ae.getMessage());
        }
    }


    //拿到所有应用
    @Override
    public List getMoreEntertainment() {
        List<Application> applications = applicationMapper.selectAll();
        if (applications.isEmpty()) {
            log.warn("未找到任何其他应用", new AppException(ResponseEnum.NO_ANY_REDUNDANT_APP));
            throw new AppException(ResponseEnum.NO_ANY_REDUNDANT_APP);
        }
        return applications;
    }

    @Override
    public void download(PlayerDTO playerDTO) {

    }

}
