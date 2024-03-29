/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.flashka.web.taxi.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

import java.util.Date

@Controller
class BaseController {

    @Value("\${application.message:Hello World}")
    private val message = "Content"

    @GetMapping("/")
    fun welcome(model: MutableMap<String, Any>): String {
        model["time"] = Date()
        model["message"] = this.message
        return "welcome"
    }

    /*@RequestMapping("/fail")
	public String fail() {
		throw new MyException("Oh dear!");
	}

	@RequestMapping("/fail2")
	public String fail2() {
		throw new IllegalStateException();
	}

	@ExceptionHandler(MyException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody
    MyRestResponse handleMyRuntimeException(MyException exception) {
		return new MyRestResponse("Some data I want to send back to the client.");
	}*/

}
