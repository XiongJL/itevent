package com.liwinon.itevent.qywx;

public enum WxConfig {
    Token("j2guDGx"), //IT应用接收消息接口Token
    EncodingAESKey("f3DERzDMO2ys9X4W0F7YNDkN353F93toR86UGDKoK7u"), //IT应用接收消息接口秘钥
    ReceiveURL("https://mesqrcode.liwinon.com/itevent/receive"), //企业微信接收/回调 接口.
    Corpid("wwbc7acf1bd2c6f766"), // 企业ID
    IThelpId("1000019"), // it服务支持应用ID
    IThelpSecret("bnhD7cALjA5yLjzUsdd-eZAmObJw87dGRO-d8dsBUow"), // it服务支持应用秘钥
    TokenUrl("https://qyapi.weixin.qq.com/cgi-bin/gettoken") ,  //获取Token的接口地址
    ApplyURL("https://mesqrcode.liwinon.com/itevent/initiation"),      //申请服务的地址
    QEventURL("https://mesqrcode.liwinon.com/itevent/qevent"),  //点击 查询返回的事件进度页面
    ;
    String value ;
    WxConfig(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
