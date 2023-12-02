package com.alinesno.infra.data.stream.exchange.common.util;


//import com.alinesno.infra.data.stream.api.dto.UserSession;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;

/**
 * @author zhp
 * @Description:
 * @date 2020-08-12
 * @time 21:25
 */
public class UserSessionUtil {

//  //日志记录
//  private static final Logger log = LoggerFactory.getLogger(UserSessionUtil.class);
//
//  /**
//   * 根据cookie获取登陆信息
//   *
//   * @author zhp
//   * @date 2020-08-12
//   * @time 19:06
//   */
//  public static UserSession userSession(HttpServletRequest request) {
//    if (request == null || request.getCookies() == null) {
//      return null;
//    }
//    Cookie[] cookies = request.getCookies();
//
//    for (Cookie cookie : cookies) {
//      if (cookie.getName().equals(SystemConstants.COOKIE_NAME_SESSION_ID)) {
//        try {
//          if (StringUtils.isEmpty(cookie.getValue())) {
//            log.warn("登陆信息失效 请重新登陆");
//            return null;
//          }
//          UserSession userSession = UserSession
//              .toUserSession( Base64Coded.decode(cookie.getValue().getBytes()));
//          return userSession;
//        } catch (Exception e) {
//          log.error("解析登陆信息 请重新登陆 {}", cookie.getValue(), e);
//        }
//      }
//    }
//
//    return null;
//  }
}
