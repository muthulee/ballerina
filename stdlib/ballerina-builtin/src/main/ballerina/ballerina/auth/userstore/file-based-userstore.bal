// Copyright (c) 2018 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package ballerina.auth.userstore;

import ballerina.config;
import ballerina.security.crypto;

@Description {value:"Represents the file-based user store"}
public struct FilebasedUserstore {
}

@Description {value:"Attempts to authenticate with username and password"}
@Param {value:"username: user name"}
@Param {value:"password: password"}
@Return {value:"boolean: true if authentication is a success, else false"}
public function <FilebasedUserstore userstore> authenticate (string user, string password) (boolean) {
    string passwordHash = readPasswordHash(user);
    if (passwordHash != null && passwordHash == crypto:getHash(password, crypto:Algorithm.SHA256)) {
        return true;
    }
    return false;
}

@Description {value:"Reads the password hash for a user"}
@Param {value:"string: username"}
@Return {value:"string: password hash read from userstore, or null if not found"}
function readPasswordHash (string username) (string) {
    // first read the user id from user->id mapping
    string userid = readUserId(username);
    if (userid == null) {
        return null;
    }
    // read the hashed password from the userstore file, using the user id
    return config:getInstanceValue(userid, "password");
}

@Description {value:"Reads the user id for the given username"}
@Param {value:"string: username"}
@Return {value:"string: user id read from the userstore, or null if not found"}
function readUserId (string username) (string) {
    return config:getInstanceValue(username, "userid");
}

@Description {value:"Reads the groups for a user"}
@Param {value:"string: username"}
@Return {value:"string: comma separeted groups list, as specified in the userstore file or null if not found"}
public function <FilebasedUserstore userstore> readGroupsOfUser (string username) (string) {
    // first read the user id from user->id mapping
    string userid = readUserId(username);
    if (userid == null) {
        return null;
    }
    // reads the groups for the userid
    return config:getInstanceValue(userid, "groups");
}

