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
    Oauth2URL("https://open.weixin.qq.com/connect/oauth2/authorize?"),      //微信用户访问页面oauth2认证，获取查询用户的code码，做获取用户信息
    redirect_uri("https://mesqrcode.liwinon.com/itevent/initiation"),
    QEventURL("https://mesqrcode.liwinon.com/itevent/qEvent"),  //点击 查询返回的事件进度页面
    ScoreURL("https://mesqrcode.liwinon.com/itevent/score?uuid="),		//回访页面
    DetailsURL("https://mesqrcode.liwinon.com/itevent/details"), //查看处理中事件的详情
    QMissionURL("https://mesqrcode.liwinon.com/itevent/qMission?uuid="),  //查询任务详细界面
    //QMissionURL("http://172.60.45.198:8093/itevent/qMission?uuid="),  //测试
    QMissionScoreURLURL("http://10.205.48.59:8093/itevent/score?uuid="),  //测试
    ApplyURLcs("http://10.205.48.59:8093/itevent/initiationindex"),  //测试
    UserIdURL("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo"),  //获取页面访问用户userid
    ;
    String value ;
    WxConfig(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
