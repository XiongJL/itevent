package com.liwinon.itevent.controller.event;

import com.liwinon.itevent.annotation.PasssToken;
import com.liwinon.itevent.qywx.AccessToken;
import com.liwinon.itevent.qywx.WxApi;
import com.liwinon.itevent.qywx.WxConfig;
import com.liwinon.itevent.service.InitiationService;
import com.liwinon.itevent.util.HttpUtil;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


@Controller
@RequestMapping("/itevent")
public class initiationController {
	
	/**https://open.weixin.qq.com/connect/oauth2/authorize?
	 * appid=CORPID&redirect_uri=REDIRECT_URI
	 * &response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
	 * 	appid	是	企业的CorpID
		redirect_uri	是	授权后重定向的回调链接地址，请使用urlencode对链接进行处理
		response_type	是	返回类型，此时固定为：code
		scope	是	应用授权作用域。企业自建应用固定填写：snsapi_base
		state	否	重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
		#wechat_redirect	是	终端使用此参数判断是否需要带上身份信息
		员工点击后，页面将跳转至 redirect_uri?code=CODE&state=STATE，企业可根据code参数获得员工的userid。code长度最大为512字节。
	 * */
	/**
	 * 构造参数并将请求重定向到微信API获取登录信息
	 *https://open.weixin.qq.com/connect/oauth2/authorize?appid=wwbc7acf1bd2c6f766&redirect_uri=https://mesqrcode.liwinon.com/itevent/initiation&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect
	 * @return
	 */
	@GetMapping(value = "/initiationindex")
	@PasssToken
	public String Oauth2API(HttpServletRequest request,HttpServletResponse res) {
		HttpSession session = request.getSession();
		String models=(String) session.getAttribute("AccessMode");
		if("pc".equals(models)) {			
			return "event/initiationpc";
		}else {   //安卓   安卓微信    企业微信   都可以用这个也买你
			System.out.println("=========---------------------");
				String corpid="wwbc7acf1bd2c6f766";
		        String redirect_uri = "https://mesqrcode.liwinon.com/itevent/initiation";
		        System.out.println("转码后的uri为:"+redirect_uri);
		        String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?"+
		                "appid="+corpid+
		                "&redirect_uri="+redirect_uri+
		                "&response_type=code"+
		                "&scope=snsapi_base"+
		                "&state=STATE#wechat_redirect";  //state不是必须,重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
		        //员工点击后，页面将跳转至 redirect_uri?code=CODE&state=STATE    code用以换取userid
		        try {
		            res.sendRedirect(URL); //重定向
		        } catch (IOException e) {
		            System.out.println("重定向到OAutho2.0失败!");
		            e.printStackTrace();
		        } catch (IllegalStateException e){
		            System.out.println("捕获到 sendRedirect异常!");
		            //重定向到 err?  ,错误页面直接跳转到主页?  cookie 永久保存(最后做.)?
		        }
		}
		// 此处可以添加获取持久化的数据，如企业号id等相关信息
		return null;
	}
 
	@Autowired
	WxApi wxApi;
	@GetMapping(value = "/initiation")
    @PasssToken
    public String initiation(String code, String state, HttpServletRequest request,Model model){
		HttpSession session = request.getSession();
		String models=(String) session.getAttribute("AccessMode");
		if("pc".equals(models)) {			
			return "event/initiationpc";
		}else {   //安卓   安卓微信    企业微信   都可以用这个也买你
			String userid = wxApi.getUseridByCode(code);
			System.out.println("====================="+userid+"===========================");
			String wxuserid=userid;    //获取访问人员userid
			if(!"".equals(wxuserid)) {
				model.addAttribute("wxuserid", wxuserid);
			}
			return "event/initiationmobile";
		}
    }
	
	@Autowired
	InitiationService initiationService;
	
	@GetMapping(value="/initiation/level_1")
	@PasssToken
	@ResponseBody
	public JSONObject level_1(){
		return initiationService.level_1();
	}
	@GetMapping(value="/initiation/level_2")
	@PasssToken
	@ResponseBody
	public JSONObject level_2(HttpServletRequest request,HttpServletResponse response){
		return initiationService.level_2(request);
	}
	@GetMapping(value="/initiation/description")
	@PasssToken
	@ResponseBody
	public JSONObject description(HttpServletRequest request,HttpServletResponse response){
		return initiationService.description(request);
	}
	@PostMapping(value="/initiation/initiationpc")
	@PasssToken
	@ResponseBody
	public JSONObject initiationpc(HttpServletRequest request,HttpServletResponse response,
			@RequestParam("file")MultipartFile[] file, @RequestParam("userid")String userid
			, @RequestParam("phone")String phone,@RequestParam("adminuser")String adminuser,
			@RequestParam("level_1")String level_1
			, @RequestParam("level_2")String level_2, @RequestParam("description")String description
			, @RequestParam("type")String type, @RequestParam("brand")String brand
			, @RequestParam("itemid")String itemid, @RequestParam("remark")String remark){
		return initiationService.initiationpc(request,file,userid,phone,adminuser,level_1,level_2,description,type,brand,itemid,remark);
	}
	@PostMapping(value="/initiation/initiationmobile")
	@PasssToken
	@ResponseBody
	public JSONObject initiationmobile(HttpServletRequest request,HttpServletResponse response){
		//创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断request是否有文件需要上传
        List<MultipartFile> file = null ;
        String userid=request.getParameter("userid");
        String phone=request.getParameter("phone");
        String adminuser=request.getParameter("adminuser");
        String level_1=request.getParameter("level_1");
        String level_2=request.getParameter("level_2");
        String description=request.getParameter("description");
        String type=request.getParameter("type");
        String brand=request.getParameter("brand");
        String itemid=request.getParameter("itemid");
        String remark=request.getParameter("remark");
        if(multipartResolver.isMultipart(request)){
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            file = multiRequest.getFiles("file");
        }
		return initiationService.initiationmobile(request,file,userid,phone,adminuser,level_1,level_2,description,type,brand,itemid,remark);
	}
	
	/**
	 * 构造带员工身份信息的URL
	 * @param corpid  企业id
	 * @param redirect_uri  授权后重定向的回调链接地址，请使用urlencode对链接进行处理
	 * @param state 重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值
	 */
	private String oAuth2Url(String corpid, String redirect_uri) {
		/*try {
			redirect_uri = java.net.URLEncoder.encode(redirect_uri, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		String oauth2Url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + corpid + "&redirect_uri=" + redirect_uri
				+ "&response_type=code&scope=snsapi_base&state=sunlight#wechat_redirect";
		System.out.println("oauth2Url=" + oauth2Url);
		return oauth2Url;
	}
	
}
	
