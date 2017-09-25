package com.auth0.controller;

import com.auth0.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@Controller
@RequestMapping("digital/password/")
public class PasswordController {

  @Autowired
  AppConfig appConfig;

  @Autowired
  private com.auth0.service.ApiService apiService;

  protected static final  HashMap<String, String>errorMap(final  HttpServletResponse res, final String message) {
    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    final HashMap<String, String> map = new HashMap<>();
    map.put("success", "false");
    map.put("message",message);
    return map;
  }

  @RequestMapping(value = "update", method = RequestMethod.GET, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Map<String, String> changePassword(@RequestParam("newPassword") String newPassword, HttpServletRequest request, final HttpServletResponse res) {

    // just tiny validation on length... ;)
    if (newPassword == null || newPassword.length() < 3) {
      return errorMap(res,"newPassword cannot be null or less than 3 chars");
    }

    final String tokenWithBearerPrefix = request.getHeader("Authorization");
    final String token = tokenWithBearerPrefix.substring("Bearer ".length());

    try {
      final DecodedJWT jwt = JWT.decode(token);
      final Map<String, Claim> claims = jwt.getClaims();
      final Claim claim = claims.get("sub");

      if (claim == null) {
        return errorMap(res, "claim cannot be null");
      }

      final String auth0UserId = claim.asString();

      // Get an Auth0 Management Token
      final String mgmtToken = apiService.getAuth0MgmtToken();
      System.out.println(mgmtToken);

      // PATCH user with new email
      final boolean success = apiService.updateUserPassword(mgmtToken, auth0UserId, newPassword);

      if (! success) {
        return errorMap(res,"Failed to update password..");
      }

      System.out.println("Password successfully updated");

      final HashMap<String, String> map = new HashMap<>();
      map.put("success", "true");
      return map;

    } catch (IOException e) {
      final String errMessage = e.getMessage();
      System.out.println(errMessage);
      return errorMap(res, errMessage);
    }

  }

}
