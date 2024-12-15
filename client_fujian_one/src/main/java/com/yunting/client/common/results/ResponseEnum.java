package com.yunting.client.common.results;


import com.yunting.client.common.utils.FS;

public enum ResponseEnum {

    // 系统的·正常和异常情况
    SUCCESS("200", "请求成功-SUCCESSFUL"),
    NOTICE_MSG_MODIFYED("10086", ""),
    SYSTEM_ERROR("500", "未知错误，联系管理员"),
    ARITHMETIC_EXCEPTION("5001", "算术异常"),
    MISSING_REQUEST_PARAMETER_EXCEPTION("5003", "请求参数丢失"),

    UNAUTHORIZED_REQUEST("401", "未身份验证的请求"),
    //  403 与 401 Unauthorized 不同，服务器知道客户端的身份。 (可参考request响应状态码),
    FORBIDDEN_REQUEST("403", "未授权的请求"),


    //minio相关
    MINIO_SERVER_ERROR("33070", "服务器异常,请检查相应服务器"),
    MINIO_INSUFFICIENT_DATA_ERROR("33075", "数据不足无法完成该种请求操作,请检查上传对象数据是否完整或有丢失"),
    MINIO_ERROR_RESPONSE("33078", "错误的响应"),
    MINIO_INVALID_ERROR_RESPONSE("33076", "无效或未知的响应"),
    MINIO_XML_PARSE_ERROR("33063", "xml文件解析失败"),
    MINIO_NO_SUCH_ALGORITHM_ERROR("33068", "特定加密算法在环境中不可用"),
    MINIO_INVALID_KEY_ERROR("33009", "无效的key,请检查key和授权令牌是否正确"),
    MINIO_INTERNAL_ERROR("33001", "服务内部发生错误或异常"),

    DATA_NOT_FOUND("404", "未存在的资源请求"),
    REQUEST_METHOD_NOT_SUPPORTED("405", "目标资源不支持该方法"),

    PAYLOAD_TOO_LARGE("413", "请求体量过大"),
    UPLOAD_FILE_TOO_MUCH("41980", "上传文件数量过多,请分批次上传"),
    FILE_EMPTY_ERROR("41000", "数据为空，请重新导入"),
    FILE_FULL_HANDLE_ERROR("41033", "数据量过大，请分批导入"),
    FILE_READ_END_ERROR("412313", "文件读取已达至末尾"),
    IMG_ALREADY_EXIST("419901", "图片已存在,请重新选择"),

    // 安全校验和权限认证方面
    TOO_MANY_REQUEST("429999", "请求次数过多，请稍后再试"),
    PASSWORD_FAILED("80502", "密码错误"),
    CALLBACK_USER_ID_ERROR("80503", "回调用户ID不匹配"),
    CALLBACK_TRANS_ID_ERROR("80504", "回调交易ID不匹配"),

    IMG_NOT_MATCH_RESPONSE("50575", "图片未找到或图片hash值不匹配,请使用正确的图片"),
    GENERATE_KEY_PAIR_ERROR("11111", "密钥对生成失败"),
    KEY_PAIR_NOT_FOUND("10080", "密钥对未找到"),

    GENERATE_VERIFICATION_CODE_ERROR("10085", "验证码生成失败"),
    VERIFICATION_CODE_SAVE_ERROR("10084", "验证码保存失败"),
    REDIS_CONNECTION_ERROR("10086", "Redis连接失败"),

    ADV_ENCOURAGE_SIGN_ERROR("80502", "激励广告回传签名不匹配,请使用正确的签名"),
    TOKEN_INVALIDATE("80545", "无效Token ,请使用正确的Token"),
    TOKEN_EXPIRE("80501", "Token过期 ,请重新登录"),
    COMPRESS_JWT_FAILED("80504", "压缩JWT失败"),
    DECOMPRESS_JWT_FAILED("80505", "解压JWT失败"),
    USER_NO_LOGIN("80401", "用户未登录,请先登录"),
    REGISTER_FAILED("80503", "用户注册失败"),
    BAN_USER_OUT("80504", "您的账号已被封禁，请联系管理员"),

    GATHERING_OVER_FLOW("80595", "在线人数超出聚集设置上限,请重新登录"),
    PLAYER_NOT_PARTICIPATE_IN_GATHERING("80556", "玩家未参与聚集校验,请先去聚集校验"),
    PLAYER_WITHOUT_UPLOAD_DEVICE_INFO("87662", "玩家似乎未上传设备信息,或未找到该玩家的设备信息"),


    NO_ANY_REDUNDANT_APP("80888", "更多游戏待上新...,敬请期待"),
    //    参数问题
    PARAMETER_NOT_FOUND("4090", "请求参数不存在"),

    PARAMETER_VALIDATION_ERROR("4091", "参数校验失败"),

    REQUEST_ACCESS_TOKEN_FAILED("76030", "请求微信Token失败"),
    REQUEST_WECHAT_USERINFO_FAILED("76033", "获取用户信息失败"),


    //    添加 删除 ,修改,查找数据
    SELECT_DATA_FAILED("66030", "查询目标数据失败"),
    PLAYER_NO_SEE_ENCOURAGE("67667", "用户未观看激励广告,请先去观看激励广告再来发起提现"),
    WITHDRAW_PRE_WATCH("88066", "提现广告已观看,请立马前往提现"),
    PLAYER_NO_REAL_IDENTIFY("55002", "用户未完成实名认证,请优先去实名认证"),
    PLAYER_WITHDRAW_COUNT_OVER_LIMIT("55090", "玩家提现次数已达上限,请明天再来吧"),
    PLAYER_NO_MORE_MONEY("66066", "玩家余额不足,请先充值"),
    GET_RED_PACKET_INFO_FAILED("66071", "查询该玩家红包信息失败"),
    PLAYER_WITHDRAW_FAILED("66071", "免审核提现失败,请联系管理员"),
    PLAYER_WITHDRAW_ADMIN_FAILED("66070", "审核提现失败,请联系管理员"),
    PLAYER_WITHDRAW_MONEY_TOO_SMALL("55000", "提现金额太小了"),
    PLAYER_WITHDRAW_MONEY_TOO_LARGE("55999", "提现金额过大,该次交易已被拒绝"),

    ALIPAY_TRANSFER_SUCCESSFUL("66666", "支付宝转账成功!!!"),
    ALIPAY_CLIENT_INIT_FAILED("66006", "支付宝客户端初始化失败"),
    ALIPAY_TRANSFER_INVOKE_FAILED("55005", "支付宝转账接口调用失败,请联系管理员查看详情"),
    ALIPAY_INFO_DIFFERENCE("55006", "您所提交的支付宝账号信息与上次有所不同"),
    WITHDRAW_ORDER_MENTIONED("66088", "订单已提交审核,请稍候"),
    NO_JUDGE_ORDER_SUCCESSFUL("66077", "免审核订单提现成功"),
    ALIPAY_TRANSFER_ERROR("" + FS.temp_code, "支付宝转账未成功,原因竟是....." + FS.temp_msg),
    PLAYER_ALIPAY_ALREADY_BIND("55001", "该支付宝账号已被绑定"),


    SELECT_MASTER_FAILED("66020", "未查到该账号的管理员"),
    SELECT_APPLICATION_FAILED("66021", "查询应用失败"),
    SELECT_ADVERTISE_FAILED("66022", "查询广告位失败"),
    SELECT_CODEBIT_FAILED("66023", "查询代码位失败"),
    SELECT_PLAYER_FAILED("66024", "暂无该玩家的信息"),
    ADD_LOCATION_FAILED("66025", "添加位置信息失败"),
    UPDATE_LOCATION_FAILED("66052", "位置信息更新失败"),

    MOBILE_PARAMETER_EMPTY("66026", "手机设备参数为空"),
    DEVICE_OVER_FLOW("66028", "该设备型号数量已达上限,请稍后再试"),
    SAME_MAC_OVER_FLOW("66029", "该MAC地址人数已达上限,请稍后再试"),
    DEVICE_RECORD_NOT_FOUND("66025", "未查到该玩家有过登录的设备记录"),
    DEVICE_NOT_MATCH("66027", "登录异常,请检查设备是否更换"),

    ADV_ENCOURAGE_NOT_EXIST_CALLBACK("66624", "该条激励广告记录未找到,服务端回调失败"),
    GET_GAME_SETTING_FAILED("66155", "查询游戏设置失败"),
    GET_ADV_INTERVAL_TIME_FAILED("66146", "查询广告的间隔时间失败"),
    GET_USER_REWARD_SETTING_FAILED("66747", "查询用户奖励比例设置失败"),
    GET_ANTI_REWARD_SETTING_FAILED("66081", "查询限制用户设置失败"),
    GET_RISK_CONTROL_SETTING_FAILED("66411", "查询风控设置失败"),
    GET_USER_GATHERING_SETTING_FAILED("66030", "获取用户聚集设置失败"),

    DATA_IS_EXIST("55001", "目标数据已存在"),

    ADD_DATA_ERROR("55030", "添加目标数据失败"),
    //    用户操作
    EDIT_OPERATION_MSG_FAILED("55011", "编辑操作原因失败"),
    EDIT_DEVICE_NAME_FAILED("55022", "编辑设备名称失败"),
    RECORD_BEHAVE_ADD_FAILED("55012", "添加用户行为记录失败"),
    DEVICE_RECORD_ADD_Failed("55031", "添加设备记录失败"),
    MOBILE_ID_NOT_MATCH("55088", "未能匹配的设备id,请检查设备ID是否篡改"),
    MOBILE_INFO_ADD_FAILED("55099", "添加手机详细信息出错"),
    CHANGE_USER_OPERATION_FAILED("55013", "针对用户的操作失败"),

    ALEREADY_BANNED("55014", "用户已被封禁,无需在封禁"),
    ALEREADY_UNBANED("55014", "用户已解封,无需再解封"),
    ALEREADY_SPECIAL("55014", "用户已是特殊用户,无需在委派"),
    ALEREADY_UNSPECIAL("55014", "用户已是普通用户,无需在撤销"),
    MAN_IDENTIFY_FAILED("55015", "用户实名信息更新失败,请联系管理员操作..."),


    ADD_MASTER_FAILED("55020", "添加管理员失败"),
    ADD_APPLICATION_FAILED("55021", "添加应用失败"),
    ADD_ADVERTISE_FAILED("55022", "添加广告位失败"),
    ADD_CODEBIT_FAILED("55023", "添加代码位失败"),
    ADD_DAILY_TASK_FAILED("55024", "添加每日任务失败"),
    SAVE_DEVICE_RECORD_FAILED("55025", "保存设备记录失败"),

    SAVE_AD_ENCOURAGE_FAILED("55051", "添加激励广告失败"),
    SAVE_AD_STREAM_FAILED("55052", "添加信息流广告失败"),
    SAVE_AD_OPENSCREEN_FAILED("55053", "添加开屏广告失败"),
    SAVE_AD_INSCREEN_FAILED("55054", "添加插屏广告失败"),
    SAVE_AD_ROWSTYLE_FAILED("55055", "添加横幅广告失败"),


    UPDATE_DATA_ERROR("55050", "修改目标数据失败"),
    //    激励广告 全步骤
    ADD_AD_ENCOURAGE_FAILED("55750", "添加激励广告失败"),
    UPDATE_AD_ENCOURAGE_REWARD_FAILED("55759", "设置激励广告奖励值出错"),
    UPDATE_AD_ENCOURAGE_WATCH_FAILED("55752", "更改激励广告观看并上传记录失败"),
    UPDATE_AD_ENCOURAGE_LOAD_FAILED("55753", "更改激励广告加载并上传记录失败"),
    RECORDIING_AD_ENCOURAGE_FIRST_CLICK_TIME_RECORD_FAILED("55753", "更改激励广告首次点击时间,修改记录失败"),
    UPDATE_AD_ENCOURAGE_GET_WARD_FAILED("55754", "更改激励广告收下奖励,修改记录失败"),
    CHANGE_AD_ENCOURAGE_RECORD_FAILED("55751", "激励广告完成观看步骤,修改记录失败"),
    UPDATE_AD_ENCOURAGE_ENOUGH_FAILED("55753", "激励广告达到奖励条件步骤,修改记录失败"),


    UPDATE_AD_OPEN_CLOSE_FAILED("55052", "更改开屏广告关闭状态失败"),
    AD_CLICK_COMPENSATE_FAILED("55099", "点击量补发失败,详情请联系操作人员"),
    CLICK_AD_CLICK_LIMIT("55053", "广告的点击次数已到达上限"),
    STORING_AD_CLICK_AND_DELETE_REDIS_KEY_FAILED("55053", "存储广告的点击次数并清除redis失败"),
    UPDATE_AD_IN_CLOSE_FAILED("55053", "更改插屏广告关闭状态失败"),
    UPDATE_AD_STREAM_CLOSE_FAILED("55054", "更改信息流广告关闭状态失败"),
    UPDATE_AD_ROWSTYLE_CLOSE_FAILED("55055", "更改横幅广告关闭状态失败"),
    UPDATE_AD_ENCOURAGE_CLOSE_FAILED("55056", "更改激励广告关闭状态失败"),

    CHANGE_AD_STREAM_FAILED("55752", "更改信息流广告失败"),
    CHANGE_AD_OPENSCREEN_FAILED("55753", "更改开屏广告失败"),
    CHANGE_AD_INSCREEN_FAILED("55754", "更改插屏广告失败"),
    CHANGE_AD_ROWSTYLE_FAILED("55755", "更改横幅广告失败"),


    UDATE_GAME_SETTING_FAILED("55051", "修改游戏设置失败"),

    UDATE_USERGATHERING_SETTING_FAILED("55052", "修改聚集设置失败"),

    UDATE_USERREWARD_SETTING_FAILED("55053", "修改用户奖励比例设置失败"),

    UDATE_ANTIAD_SETTING_FAILED("55054", "修改限制观看广告设置失败"),

    UPDATE_RISKCONTROL_SETTING_FAILED("55055", "修改风控设置失败"),


    //    websocket连接 心跳检测相关,
    WEBSOCKET_CONNECTION_FAILED("898800", "websocket连接失败"),

    WEBSOCKET_CONNECTION_TIMEOUT("898801", "websocket连接超时"),

    INVALIDATE_SESSION_CLOSE_ERROR("898802", "已断开的连接关闭异常"),

    NONE_CLIENT_CONNECT("898810", "暂无客户端连接"),

    SEND_MESSAGE_FAILED("898891", "Websocket消息发送失败");


    public String codeqwe;
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // 没有public  这个构造方法 不能用到外面去  只能在这个类中被使用（提示的信息 只能在内部生成）
    ResponseEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
