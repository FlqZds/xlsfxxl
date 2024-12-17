package com.yunting.service;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

public class WlanService {
    @Resource(name = "RedisUtil_Record")
    private RedisUtil_Record rur;

    @Resource(name = "RedisUtils")
    private RedisUtils_Wlan rs;

    @Resource(name = "RedisUtil_session")
    private RedisUtil_session rus;

    /***
     * 存储新用户的WiFi列表和更新该地区的用户列表
     * @param wlanList 新上线玩家此时的wifi列表
     * @param playerId 新上线的id
     * @param location 新上线玩家此时的位置
     */
    public void buildWiFiRelation(List<String> wlanList, String playerId, String location) {
        //存储新玩家的wifi列表和 更新该位置的用户列表
//        if (rs.setMembers(playerId).size() > 0) {
//
//            for (String player_old_mac : rs.setMembers(playerId)) {//删掉该玩家原有的wifi列表
//                if (rs.setMembers(location + player_old_mac).size() - 1 == 0) {
//                    rs.sAdd(location + player_old_mac, "");
//                }
//                rs.sRemove(location + player_old_mac);  //该位置用户id列表
//
//                if (rs.setMembers(player_old_mac).size() - 1 == 0) {
//                    rs.sAdd(player_old_mac, "");
//                }
//                rs.sRemove(player_old_mac); //该位置相关mac列表
//            }
//            rs.delete(playerId);
//        }

        Object o = rs.hGet(location + "AL", playerId);
        if (o != null) {         //同时该玩家原有的位置+序号也需要删掉  (如果该位置有的话)
            String location_index = o.toString();
            rs.hDelete(location, location_index);
            rs.hDelete(location + "AL", playerId);
        }

        for (String wlan : wlanList) {
            rs.sAdd(playerId, wlan);  //新用户的wifi列表
        }

        String thisID = null; //新玩家添加到该位置的在线用户列表 ,没有就直接插  有的话 就做一些非空处理并防止重复插入 在插入
        if (rs.hExists(location, rs.hSize(location) + "") == true) {
            thisID = rs.hGet(location, rs.hSize(location) + "") + "";
            if (thisID.equals(playerId) == false) {
                rs.hPut(location, (rs.hSize(location) + 1) + "", playerId);
                rs.hPut(location + "AL", playerId, rs.hSize(location) + "");
            }
        } else {
            rs.hPut(location, (rs.hSize(location) + 1) + "", playerId);
            rs.hPut(location + "AL", playerId, rs.hSize(location) + "");
        }

    }

    //写个方法recheck() 给list_recheck做聚集判断用 方法返回的是需要踢掉的人的id列表'

    /***
     *
     * @param list_kick         聚集要踢的列表
     * @param list_device_kick  要踢的同型号列表
     * @param playerId          和新上线用户有聚集关系的用户id
     * @param location      新上线玩家此时的位置
     * @param pop_l     大聚集人数上限
     * @param pop_s     小聚集人数上限
     * @param choice    大聚集还是小聚集
     * @param original_A  新登用户
     * @param isSameDevice  是否同台设备
     * @param deviceLimit   同型号上限
     */
    public void reCheck(Set<String> list_kick, Set<String> list_device_kick, String playerId, String location, Integer pop_l, Integer
            pop_s, Character choice, String original_A, boolean isSameDevice, Integer deviceLimit) {

        synchronized (this) {
            List<String> about_a_id_1 = new ArrayList<>();                                           //跟a 一步有关系的用户id列表
            Set<String> list_a = rs.setMembers(playerId);
            Set<String> list_same_device_recheck = new HashSet<>();                                     //同型号设备列表


            Set<String> this_All_mac = new HashSet<>();                       //wifi关系表中的 该位置所有mac
            this_All_mac.addAll(rs.setMembers(location + "MAC"));               //新用户上来之前的MAC关系表

            for (String wlan : list_a) { //遍历a的wifi列表
                log.info(playerId + "recheck中的WiFi列表:" + wlan);
                for (String mac : this_All_mac) {//遍历该定位下的wifi关系表 的 mac字段
                    log.info(playerId + "recheck中遍历一步的MAC:" + mac);
                    if (mac.equals(wlan)) {
                        // 取wifi列表里 该mac对应的wifi相关id列表
                        Set<String> list_id = rs.setMembers(location + mac);
                        if (list_id.size() != 1) { // 记一步有关联的用户
                            for (String id : list_id) {
                                if (!about_a_id_1.contains(id)) {
                                    about_a_id_1.add(id);
                                    log.info(playerId + "recheck中一步用户:" + id + "成功加入");

                                    //用一个Set存其中与其同型号id
                                    if (this.isSameDevice(id, playerId) == true) {
                                        list_same_device_recheck.add(id);
                                    }
                                }
                            }
                        }

                        //取wifi列表里 该mac对应的相关mac列表  见上面的说明
                        Set<String> list_mac = rs.setMembers(mac);
                        for (String mac_1 : list_mac) {
                            log.info(playerId + "recheck中遍历二步的MAC:" + mac);
                            // 取wifi列表里 该mac对应的wifi相关id列表
                            Set<String> list_id_1 = rs.setMembers(location + mac_1);
                            if (list_id_1.size() != 1) { // 记二步有关联的用户
                                for (String id : list_id_1) {

                                    if (!about_a_id_1.contains(id)) {
                                        about_a_id_1.add(id);
                                        log.info(playerId + "recheck中二步用户:" + id + "成功加入");

                                        //用一个Set存其中与其同型号id
                                        if (this.isSameDevice(id, playerId) == true) {
                                            list_same_device_recheck.add(id);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }

            if (about_a_id_1.contains("")) {
                about_a_id_1.remove("");
            }

            if (about_a_id_1.contains("")) {
                about_a_id_1.remove("");
            }


            log.info("ReCheck函数的 ==:>:" + "about_a_id_1人数:" + about_a_id_1 + "about_a_id_1人数:" + about_a_id_1 + "上限:" + pop_l);
            //大聚集 一步+两步的人加起来 比设置的值还大时 就得踢掉包括一步+两步+A的所有人 因为只踢A 剩下的人还是会触发大聚集
            if (about_a_id_1.size() > pop_l) {
                log.info(playerId + "recheck大聚集踢所有人");
                //list_kick加入list_about_a1和list_about_a2里的所有用户和A
                list_kick.addAll(about_a_id_1);
                list_kick.add(original_A);
                log.info("recheck大聚集踢人列表:" + list_kick);
            } else if (about_a_id_1.size() == pop_l) {
                log.info(playerId + "_:recheck大聚集踢A");
                list_kick.add(original_A);
                log.info("recheck大聚集踢人列表:" + list_kick);
            }

            if (list_same_device_recheck.size() > deviceLimit) {
                log.info(playerId + "_:recheck同型号踢所有人");
                list_device_kick.addAll(list_same_device_recheck);
                if (isSameDevice == true) {
                    log.info(playerId + "_:recheck同型号踢所有人也踢A");
                    list_device_kick.add(original_A);
                }
                log.info("recheck同型号踢人列表:" + list_device_kick);
            } else if (isSameDevice == true && (list_same_device_recheck.size() == deviceLimit)) {
                log.info(playerId + "_:recheck同型号踢A");
                list_device_kick.add(original_A);
                log.info("recheck同型号踢人列表:" + list_device_kick);
            }

        }
    }


    /***
     * 这两个playerID的设备ID是否相同
     * @return
     */
    public boolean isSameDevice(String playerID, String compareID) {
        if (playerID == null || compareID == null || playerID.isEmpty() || compareID.isEmpty()) {
            return false;
        }

        Long player_MobileID = playerMapper.selectMobileIDByPlayerId(playerID);
        Long compare_MobileID = playerMapper.selectMobileIDByPlayerId(compareID);
        String player_MobileName = playerMapper.selectMobileNameByPlayerId(playerID);
        String compare_MobileName = playerMapper.selectMobileNameByPlayerId(compareID);
        log.info("玩家:_" + playerID + "的设备id" + player_MobileID);
        log.info("比对玩家:_" + compareID + "的设备id" + compare_MobileID);

        if (player_MobileID == null || compare_MobileID == null) {
            log.error("玩家:" + playerID + "未上传设备信息");
            throw new AppException(ResponseEnum.PLAYER_WITHOUT_UPLOAD_DEVICE_INFO);
        }

        if (player_MobileID == compare_MobileID) {
            log.info("比较后玩家" + playerID + "和比对玩家:_" + compareID + "设备型号相同");
            return true;
        }

        if (player_MobileID == compare_MobileID == false) {
            if (player_MobileName.equals(compare_MobileName) == true) {
                log.info("比较后玩家" + playerID + "和比对玩家:_" + compareID + "两者的设备ID不同但是设备名称相同,所以定为相同的设备型号");
                return true;
            }
        }

        log.info("比较后玩家" + playerID + "和比对玩家:_" + compareID + "设备型号不同");
        return false;
    }

    /***
     * //       检测聚集+同型号(所在地区,WiFi列表)
     * @param player     新上线玩家
     * @param wlanList   新上线玩家此时附近的WiFi列表
     * @param pop_s     小范围上限
     * @param pop_l     大范围上限
     * @param choice    0:小范围 1:大范围
     * @param deviceLimit    同型号设备上限
     * @param location   新上线玩家此时的位置
     * @return 校验结果
     */

    @Transactional(rollbackFor = Exception.class)
    public ResultMessage checkGathering(Player player, List<String> wlanList, Integer pop_s, Integer
            pop_l, Character choice, Integer deviceLimit, String location) {
        Long playerID = player.getPlayerId();
        String playerId = player.getPlayerId() + "";

        Long player_MobileID = playerMapper.selectMobileIDByPlayerId(playerID + "");
        log.info("新上来用户的设备ID:_" + player_MobileID);

//玩家自身上次聚集和这次聚集校验

        this.buildWiFiRelation(wlanList, playerId, location);

        synchronized (this) {
            log.info("当前线程名称:" + Thread.currentThread().getName());
            log.info("当前线程ID:" + Thread.currentThread().getId());
            Set<String> list_a = rs.setMembers(playerId);
            Set<String> list_a2 = new HashSet<>();                                     //a的wifi列表中 但在wifi关系表中没有的mac列表
            list_a2.addAll(list_a);

            boolean trigger = false;//是否有进入此并集逻辑中

            Set<String> list_kick = new HashSet<>();                                           //需要踢的所有用户的id列表（开始是空的）
            Set<String> list_device_kick = new HashSet<>();                                     //同型号需要踢的所有用户的id列表（开始是空的）
            List<String> about_a_id_1 = new ArrayList<>();                                           //跟a 一步有关系的用户id列表
            List<String> about_a_id_2 = new ArrayList<>();                                          //跟a 二步有关系的用户id列表
            Set<String> list_same_device = new HashSet<>();                                          //同型号列表
            Set<String> list_recheck = new HashSet<>();                                          //需要重新查聚集的用户的id列表

            Set<String> this_All_mac = new HashSet<>();                       //wifi关系表中的 该位置所有mac
            this_All_mac.addAll(rs.setMembers(location + "MAC"));
            log.info("新用户上来之前的MAC关系表:" + this_All_mac);

            for (String wlan : wlanList) { //遍历a的wifi列表
                for (String mac : this_All_mac) {//遍历该定位下的wifi关系表 的 mac字段
                    log.info("遍历WiFi关系表的MAC:" + mac);
                    if (mac.equals(wlan)) {
                        // 取wifi列表里 该mac对应的wifi相关id列表
                        Set<String> list_id = rs.setMembers(location + mac);
                        if (list_id.size() != 1) { // 记一步有关联的用户
                            for (String id : list_id) {
                                if (!about_a_id_1.contains(id)) {
                                    about_a_id_1.add(id);
                                    if (this.isSameDevice(id, playerId) == true) {
                                        list_same_device.add(id);
                                    }
                                }
                            }
                        }

                        //取wifi列表里 该mac对应的相关mac列表  见上面的说明
                        Set<String> list_mac = rs.setMembers(mac);
                        for (String mac_1 : list_mac) {
                            // 取wifi列表里 该mac对应的wifi相关id列表
                            Set<String> list_id_1 = rs.setMembers(location + mac_1);
                            if (list_id_1.size() != 1) { // 记二步有关联的用户
                                for (String id : list_id_1) {
                                    if (!about_a_id_1.contains(id) && !about_a_id_2.contains(id)) {
                                        about_a_id_2.add(id);
                                        if (this.isSameDevice(id, playerId) == true) {
                                            list_same_device.add(id);
                                        }
                                    }
                                }
                            }
                        }

                        for (String s : list_a) {

                            //if(存在一个或多个mac，它在list_a里有，而在maci对应的 '与该mac关联的mac（列表）'里没有)  //意味着wifi关系表需要更新
                            if (rs.setMembers(mac).contains(s) == false) {
                                log.info("找到是这个:+++>" + s);
                                trigger = true;

                                //更新wifi关系表
                                // 把 mac 对应的 '与该mac关联的mac（列表）'字段更新为'与该mac关联的mac（列表）'与list_a的并集，并集里不包括ai
                                Set<String> s_union = rs.sUnion(mac, playerId);
                                s_union.remove(mac);
                                rs.delete(mac);
                                for (String mac_1 : s_union) {
                                    rs.sAdd(mac, mac_1);
                                }
                                break;
                            }
                        }

                        list_a2.remove(wlan);
                        break;
                    }
                }
            }

            if (about_a_id_2.contains("")) {
                about_a_id_2.remove("");
            }

            if (about_a_id_1.contains("")) {
                about_a_id_1.remove("");
            }

            if (about_a_id_2.contains(playerId)) {
                about_a_id_2.remove(playerId);
            }

            if (about_a_id_1.contains(playerId)) {
                about_a_id_1.remove(playerId);
            }

            log.info("list_a2: " + list_a2);

//继续更新wifi关系表
            if (list_a2.size() > 0) {
                for (String ai : list_a2) //遍历A的wifi列表中没在wifi关系表里的mac
                {
                    //wifi关系表中新增一条
                    rs.sAdd(location + ai, ""); //mac关系表 的用户id + mac
                    //                rs.sRemove(location + ai, "");

                    //mac关系表 的相关联mac   为不包括ai的list_a
                    Set<String> set = rs.setMembers(playerId);
                    set.remove(ai);
                    for (String id : set) {
                        rs.sAdd(ai, id);
                    }
                    this_All_mac.add(ai);
                    rs.sAdd(location + "MAC", ai);
                }

            }

            for (Object value : rs.hGetAll(location).values()) {
                String s = value.toString();
                for (String sm : rs.setMembers(s)) {
                    Set<String> strings = rs.setMembers(sm);
                    log.info("相关mac关系表打印:" + sm + ":  " + strings);
                    log.info("用户id列表打印:" + sm + ":  " + rs.setMembers(location + sm));
                }
            }


//处理聚集
// 先看大聚集
//一步+两步的人加起来 比设置的值还大时 就得踢掉包括一步+两步+A的所有人 因为只踢A 剩下的人还是会触发大聚集
            log.info("about_a_id_2人数:" + about_a_id_2 + "about_a_id_1人数:" + about_a_id_1 + "上限:" + pop_l);
            if (about_a_id_2.size() + about_a_id_1.size() >= pop_l) {
                log.info("大聚集触发");
                list_kick.add(playerId);
            }
            //大聚集没触发
            else if (choice == '0') {  //聚集设置选择小范围
                if (about_a_id_1.size() >= pop_s) {
                    log.info("小聚集触发");
                    list_kick.add(playerId);
                }
            }

            if (list_same_device.size() >= deviceLimit) {
                log.info("同型号触发");
                list_device_kick.add(playerId);
            }


//查看其它人是否有聚集
            if (trigger == true) {
                for (String s : about_a_id_1) {
                    this.reCheck(list_kick, list_device_kick, s, location, pop_l, pop_s, choice, playerId, this.isSameDevice(s, playerId), deviceLimit);
                }
                for (String s : about_a_id_2) {
                    this.reCheck(list_kick, list_device_kick, s, location, pop_l, pop_s, choice, playerId, this.isSameDevice(s, playerId), deviceLimit);
                }
                trigger = false;
            }

            log.info("聚集要踢的人:" + list_kick);
            log.info("同型号要踢的人:" + list_device_kick);
            //踢人
            for (String k : list_device_kick) {
                sessionUtils.userExit(k, location);
            }

            if (list_kick.contains(playerId)) {
                for (String k : list_kick) {
                    sessionUtils.userExit(k, location);
                }
                throw new AppException(ResponseEnum.GATHERING_OVER_FLOW);
            } else {
                if (list_device_kick.contains(playerId)) {
                    log.error("触发同型号", new AppException(ResponseEnum.DEVICE_OVER_FLOW));
                    throw new AppException(ResponseEnum.DEVICE_OVER_FLOW);
                } else {
                    for (String mac : this_All_mac) { //遍历该定位下的wifi关系表
                        for (String s1 : list_a) {
                            if (mac.equals(s1)) {
                                rs.sAdd(location + mac, playerId);//mac对应的 wifi相关id列表里 加上A的id
                            }
                        }
                    }
                }
            }

            return new ResultMessage(ResponseEnum.SUCCESS, null);
        }
    }


    /***
     *
     * 检测同mac下同位置的人数
     * @param player 新上线玩家
     * @param thisMac 新上线玩家的wifi
     * @param location 新上线玩家的位置
     * @param pop 同mac上限
     */
    public void checkSameMac(Player player, String thisMac, String location, Integer pop) {
        Collection<Object> values = rs.hGetAll(location + "Mac").values();
        long count = values.stream().filter(o -> o.equals(thisMac)).count();

        if (count > pop) {
            log.error(location + "同mac地址超出人数上限|>" + player.getPlayerId() + "<|玩家已记录");
            rs.hDelete(location + "Mac", player.getPlayerId() + "");  //删除该上线玩家的mac
            throw new AppException(ResponseEnum.SAME_MAC_OVER_FLOW);
        }

    }

}
