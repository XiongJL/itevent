package com.liwinon.itevent.qywx;

public enum WxConfig {
    Token("j2guDGx"), //IT应用接收消息接口Token
    EncodingAESKey("f3DERzDMO2ys9X4W0F7YNDkN353F93toR86UGDKoK7u"), //IT应用接收消息接口秘钥
    Corpid("wwbc7acf1bd2c6f766"), // 企业ID
    IThelpId("1000019"), // it服务支持应用ID
    IThelpSecret("bnhD7cALjA5yLjzUsdd-eZAmObJw87dGRO-d8dsBUow"), // it服务支持应用秘钥
    TokenUrl("https://qyapi.weixin.qq.com/cgi-bin/gettoken") ; //获取Token的接口地址
    String value ;
    WxConfig(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
