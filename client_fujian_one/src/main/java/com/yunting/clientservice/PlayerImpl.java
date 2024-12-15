package com.yunting.clientservice;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yunting.client.DTO.PlayerDTO;
import com.yunting.client.DTO.VO.WithdrawVo;
import com.yunting.client.DTO.img.ImgShow;
import com.yunting.client.DTO.img.ImgVo;
import com.yunting.client.common.exception.AppException;
import com.yunting.client.common.results.ResponseEnum;
import com.yunting.client.common.results.ResultMessage;
import com.yunting.client.common.utils.MinIoUtils;
import com.yunting.client.common.utils.RedisUtil_Record;
import com.yunting.client.common.utils.SpringRollBackUtil;
import com.yunting.client.entity.*;
import com.yunting.client.entity.setting.GameSetting;
import com.yunting.client.mapper.Client.LocationMapper;
import com.yunting.client.mapper.Client.PlayerMapper;
import com.yunting.client.mapper.Client.WithdrawRecordMapper;
import com.yunting.client.mapper.DayBehaveRecordlistMapper;
import com.yunting.client.mapper.ImageMappingMapper;
import com.yunting.client.mapper.ImgorderMapper;
import com.yunting.clientservice.service.PlayerService;
import com.yunting.forest.ForestService;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.yunting.client.common.utils.FS.IMG_TYPE;

@Slf4j
@Service("PlayerService")
@Data
public class PlayerImpl implements PlayerService {

    @Resource(name = "LocationMapper")
    private LocationMapper locationMapper;


    @Resource(name = "DayBehaveRecordlistMapper")
    private DayBehaveRecordlistMapper dayBehaveRecordlistMapper;

    @Resource(name = "PlayerMapper")
    private PlayerMapper playerMapper;

    @Resource(name = "ForestService")
    private ForestService forestService;

    @Resource(name = "WithdrawRecordMapper")
    private WithdrawRecordMapper withdrawMapper;

    @Resource(name = "MinIoUtils")
    MinIoUtils minIoUtils;

    @Resource(name = "ImageMappingMapper")
    ImageMappingMapper imageMappingMapper;

    @Resource(name = "ImgorderMapper")
    ImgorderMapper imgorderMapper;

    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;


    @Override
    public void refreshOnline(PlayerDTO playerDTO, String location) {
        String s = JSONObject.parseObject(location, JSONObject.class).get("location").toString();
        Long playerID = playerDTO.getPlayerId();
        Long gameID = playerDTO.getGameId();
        Player player = playerMapper.selectPlayerByPlayerId(playerID);
        String gameSetting_json = forestService.getGameSetting(gameID + "");
        ResultMessage resultMessage = JSONObject.parseObject(gameSetting_json, ResultMessage.class);
        GameSetting gameSetting = JSON.to(GameSetting.class, resultMessage.getData());
        String retainWay = gameSetting.getRetainWay();  //留存方式

        if (rur.getBit("retain_bitMap", playerID) == false) {

            //        位置信息插入
            Location newPlayerPosition = Location.builder()
                    .playerId(playerID)
                    .location(s)
                    .recordTime(LocalDateTime.now())
                    .build();

            //        当日留存记录 已插入
            DayBehaveRecordlist dayBehave = DayBehaveRecordlist.builder()
                    .playerId(playerID + "").appId(player.getGameId().toString())
                    .retainTime(LocalDateTime.now()).retainWay(retainWay)
                    .todayred(BigDecimal.ZERO).totalred(BigDecimal.ZERO)
                    .todayEncourageAdvCount(0)
                    .serviceCallBackAdvCount(0)
                    .serviceCallBackRewardCount(0)
                    .build();


            try {
                locationMapper.insertLocation(newPlayerPosition);

                dayBehaveRecordlistMapper.addDayBehaveRecordlist(dayBehave);

                rur.setBit("retain_bitMap", playerID, true);

                log.info("用户:" + playerID + " 十二点仍然在线,该玩家当日行为记录已留存");

            } catch (Exception e) {
                SpringRollBackUtil.rollBack();
                log.error("十二点仍然在线用户:" + playerID + " 的当日行为记录留存失败");
                throw new AppException(ResponseEnum.RECORD_BEHAVE_ADD_FAILED);
            }

        }

    }

    /***
     * 获取用户提现记录
     * @param playerDTO 玩家信息
     * @return 用户提现记录
     */
    public PageInfo getPlayerWithdrawRecord(PlayerDTO playerDTO, String packageName, Integer pageNum) {
        Long playerId = playerDTO.getPlayerId();

        PageHelper.startPage(pageNum, 50);
        List<WithdrawVo> record = withdrawMapper.getWithdrawRecordByPlayerIdAndPackageName(playerId, packageName);
        PageInfo pageInfo = new PageInfo(record);

        log.info("用户提现记录已返回,第" + pageNum + "页的");
        return pageInfo;
    }


    @Override
    public String thisPlayerInRed(PlayerDTO playerDTO) {

        BigDecimal inred = playerMapper.selectInRedByPlayerId(playerDTO.getPlayerId(), playerDTO.getGameId());

        List<DayBehaveRecordlist> daybv = dayBehaveRecordlistMapper.getDayBehaveRecordlistByPlayerId(playerDTO.getPlayerId());
        DayBehaveRecordlist dayBehaveRecord = dayBehaveRecordlistMapper.getDayLastDayBehaveRecordlistByPlayerId(playerDTO.getPlayerId());

        BigDecimal todayred = BigDecimal.ZERO;
        ;
        BigDecimal totalRed = BigDecimal.ZERO;
        ;
        if (dayBehaveRecord == null || daybv == null) {
            todayred = BigDecimal.ZERO;
            totalRed = BigDecimal.ZERO;
        } else {
            dayBehaveRecord.getTodayred();

            for (DayBehaveRecordlist d : daybv) {
                totalRed = totalRed.add(d.getTotalred());
            }

        }

        HashMap<String, Object> json_map = new HashMap<>();
        json_map.put("inred", inred);
        json_map.put("totalRed", totalRed);
        json_map.put("todayred", todayred);
        String s = JSON.toJSONString(json_map);
        return s;
    }


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
        String withdrawPercentage_str = withdrawMapper.getWithdrawPercentage(gameId);
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


    @Transactional(rollbackFor = Exception.class)
    public void preUploadFileNameAndHashVal(PlayerDTO playerDTO, List<ImgVo> imgVo, String imgType) throws IOException {
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
                //上传之前检测是否有同hash+同文件名的图片
                ImageMapping img = imgorderMapper.selectImgByImginfo(vo.getImgHash(), vo.getImgName());
                if (img != null) {
                    log.error("图片已存在,请重新选择" + vo.getImgName() + "文件hash:" + vo.getImgHash(), new AppException(ResponseEnum.IMG_ALREADY_EXIST));
                    throw new AppException(ResponseEnum.IMG_ALREADY_EXIST);
                }

                ImageMapping imageMapping = ImageMapping.builder()
                        .directory(upload_dir)
                        .fileName(vo.getImgName())
                        .fileHash(vo.getImgHash())
                        .imgType(imgType)
                        .playerId(playerDTO.getPlayerId())
                        .uploadTime(now)
                        .build();
                imageMappingMapper.insert(imageMapping);

                minIoUtils.detectBucket(gameId + "");
                minIoUtils.createDirectory(gameId + "", upload_dir); //创建图片组 相关目录
            }

        } catch (AppException ae) {
            log.error(ae.getCode(), ae.getMessage(), new AppException(ae.getCode(), ae.getMessage()));
            SpringRollBackUtil.rollBack();
            throw new AppException(ae.getCode(), ae.getMessage());
        }
    }


}
