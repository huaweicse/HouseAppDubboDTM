/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.cse.houseapp.edge;

import java.io.IOException;

import org.apache.servicecomb.common.rest.codec.RestObjectMapperFactory;
import org.apache.servicecomb.edge.core.CommonHttpEdgeDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.DynamicPropertyFactory;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.ext.web.RoutingContext;

public class AuthCommonHttpEdgeDispatcher extends CommonHttpEdgeDispatcher {
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthCommonHttpEdgeDispatcher.class);

  private static final String KEY_ENABLED = "servicecomb.http.dispatcher.edge.http.auth.enabled";

  private static final String LOGIN_PATH = "/api/login2";

  private static final String UI_PATH = "/ui/";

  @Override
  public int getOrder() {
    return 15;
  }

  @Override
  public boolean enabled() {
    return DynamicPropertyFactory.getInstance().getBooleanProperty(KEY_ENABLED, false).get();
  }

  @Override
  protected void onRequest(RoutingContext context) {
    if (isLogin(context) || isAllowedForNoLogin(context)) {
      super.onRequest(context);
      return;
    }

    Cookie sessionId = context.getCookie("session-id");
    if (sessionId == null || !isUserNameCorrect(sessionId.getValue())) {
      context.response().setStatusCode(302);
      context.response().putHeader("Location", "/ui/login.html");
      context.response().end();
      return;
    }
    context.request().headers().add("userId", sessionId.getValue());

    super.onRequest(context);
  }

  private boolean isLogin(RoutingContext routingContext) {
    if (routingContext.request().path().equals(LOGIN_PATH)) {
      return true;
    }
    return false;
  }

  private boolean isAllowedForNoLogin(RoutingContext routingContext) {
    if (routingContext.request().path().startsWith(UI_PATH)) {
      return true;
    }
    return false;
  }

  @Override
  protected Handler<Buffer> responseHandler(RoutingContext routingContext, HttpClientResponse httpClientResponse) {
    if (isLogin(routingContext)) {
      return (data) -> {
        if (httpClientResponse.statusCode() == 200) {
          long result = -1;
          try {
            result = RestObjectMapperFactory.getRestObjectMapper().readValue(data.getBytes(), Long.class);
          } catch (IOException e) {
            LOGGER.error("error parse session id", e);
          }
          Cookie cookie = Cookie.cookie("session-id", String.valueOf(result));
          cookie.setPath("/");
          cookie.setSecure(false);
          routingContext.addCookie(cookie);
          routingContext.response().write(data);
          LOGGER.info("user login successfully");
        } else {
          Cookie cookie = Cookie.cookie("session-id", String.valueOf(-1));
          cookie.setPath("/");
          cookie.setSecure(false);
          routingContext.addCookie(cookie);
          routingContext.response().setStatusCode(403);
          routingContext.response().end();
          LOGGER.info("user login failed");
        }
      };
    }
    return super.responseHandler(routingContext, httpClientResponse);
  }

  private boolean isUserNameCorrect(String userName) {
    if (userName == null) {
      return false;
    }
    long u;
    try {
      u = Long.parseLong(userName);
    } catch (NumberFormatException e) {
      return false;
    }
    if (u < 0) {
      return false;
    } else {
      return true;
    }
  }
}
