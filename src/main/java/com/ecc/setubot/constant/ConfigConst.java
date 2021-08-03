package com.ecc.setubot.constant;

public class ConfigConst extends ConstantCommon {
    public static final String ARGS_ERROR = ".config [action] [configName] [configValue]" +
            "\nsinceId,amapKey,saucenaoKey,weiboToken,weiboNewStatus,pixivImageIgnore,usePixivApi,r18,imageScaleForce,pixivImagesShowCount";
    public static final String CONFIG_NAME_EMPTY = "参数名称不能为空";
    public static final String CONFIG_VALUE_EMPTY = "参数值不能为空";
    public static final String CONFIG_SET_SUCCESS = "参数设置完成";
    public static final String CONFIG_NOT_FOUND = "没有该参数信息";

    public static final String CONFIG_R18 = "r18";
    //强制压图
    public static final String CONFIG_IMAGE_SCALE_FORCE = "imageScaleForce";

    // 撤回延迟
    public static final long RECALL_DELAY = 60000;

    public static final String SETU_CD_RESP = "这里没有色图，只有你姑奶奶";
    public static final long ADMIN_ACCOUNT = 2717318418L;
    public static final String SETU_MATCH_REGEX = "((mirai)|(竹竹))来份*(涩|色|(she))图";

    public static final String NET_EASE_IMAGE_SAVE_PATH = "data/images/neteasealbum";

}
