<template>
  <div class="form-container">
    <el-form ref="form" :model="form" label-width="20px" class="form">
      <!--账号-->
      <el-form-item>
        <el-row>
          <el-col :span="2">
            <i class="el-icon-user form-icon"></i>
          </el-col>
          <el-col :span="22">
            <el-dropdown trigger="click" v-if="dealCookie.length > 0">
              <el-input
                v-model="form.username"
                maxlength="15"
                @keyup.enter.native="loginDone('form')"
                class="form-input"
                clearable
                autofocus
                placeholder="请输入账号"
              ></el-input>
              <el-dropdown-menu
                :style="{
                  width: '300px',
                  marginLeft: '50px',
                  height: dealCookie.length < 3 ? `${dealCookie.length * 70}px` : '200px',
                }"
                slot="dropdown"
              >
                <el-scrollbar
                  style="height: 100%"
                  wrapStyle="overflow-x:hidden;padding-right:12px;"
                  viewStyle=""
                >
                  <el-dropdown-item
                    v-for="cookies in dealCookie"
                    :key="cookies.username"
                    @click.native="clickCookies(cookies)"
                    class="cookie-item"
                  >
                    <div class="cookie-content">
                      <div class="userIcon el-icon-user-solid"></div>
                      <div class="cookie-info">
                        <div class="username">{{ cookies.username }}</div>
                        <div class="password-mask">
                          <div
                            class="circle"
                            v-for="(item, index) in circleArr"
                            :key="index"
                          >{{ item }}</div>
                        </div>
                      </div>
                      <div
                        title="删除账号信息"
                        class="deleteCook el-icon-close"
                        @click.stop="deleteCookies(cookies)"
                      ></div>
                    </div>
                  </el-dropdown-item>
                </el-scrollbar>
              </el-dropdown-menu>
            </el-dropdown>

            <el-input
              v-else
              v-model="form.username"
              maxlength="15"
              @keyup.enter.native="loginDone('form')"
              class="form-input"
              clearable
              autofocus
              placeholder="请输入账号"
            ></el-input>
          </el-col>
        </el-row>
      </el-form-item>

      <!--密码-->
      <el-form-item>
        <el-row>
          <el-col :span="2">
            <i class="el-icon-lock form-icon"></i>
          </el-col>
          <el-col :span="22">
            <el-input
              type="password"
              @keyup.enter.native="loginDone('form')"
              v-model="form.password"
              maxlength="30"
              clearable
              placeholder="请输入密码"
              show-password
              class="form-input"
            ></el-input>
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item>
        <div class="remember-password">
          <el-checkbox v-model="isKeep">记住密码</el-checkbox>
        </div>
      </el-form-item>

      <el-form-item>
        <el-button
          v-if="loadingBtn === ''"
          type="primary"
          @click="login('form')"
          class="login-button"
          :disabled="loginBtn"
        >
          <span>登录</span>
        </el-button>
        <el-button
          v-else
          @click="login('form')"
          type="primary"
          class="login-button"
          :disabled="loginBtn"
        >
          {{ loadingBtn }}
        </el-button>
      </el-form-item>

      <el-form-item>
        <div class="register-link">
          <span>还没有账号？</span>
          <el-button type="text" @click="goToRegister">立即注册</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import Verify from "./verify";
import ElSelectDropdown from "element-ui/packages/select/src/select-dropdown";

export default {
  name: "Form",
  components: {
    ElSelectDropdown,
    Verify,
  },
  data() {
    return {
      loadingBtn: "",
      circleArr: ["", "", "", "", "", ""],
      isKeep: true,
      errNum: 0,
      loginState: 0,
      form: {
        username: "",
        password: "",
        level: 2,
      },
      time: "",
      loginBtn: false,
      dealCookie: [],
    };
  },
  methods: {
    clearForm(level) {
      this.form = {
        username: "",
        password: "",
        level,
      };
    },
    clickCookies(data) {
      this.form = {
        username: data.username,
        password: data.password,
        level: data.level,
      };
    },
    loginDone(formName) {
      if (!this.loginBtn) {
        this.login(formName);
      }
    },
    login(formName) {
      if(this.$refs.verify != undefined && !this.$refs.verify.check()) {
        this.$message.warning({
          message: "验证码错误",
        });
        return;
      }
      if (this.form.username === "") {
        this.$message.warning({
          message: "账号不能为空",
        });
      } else if (this.form.password === "") {
        this.$message.warning({
          message: "密码不能为空",
        });
      } else {
        this.loginState++;
        if (this.loginState === 1) {
          this.checkLogin(formName);
        }
      }
    },
    checkLogin(formName) {
      let _this = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loadingBtn = "登录中 ...";
          this.loginBtn = true;
          this.$nextTick(() => {
            _this.axiosHelper
              .get("/api/sms/user/login", {params: this.form})
              .then((response) => {
                this.loginBtn = false;
                this.loginState = 0;
                let data = response.data;
                this.click(_this, data);
              })
              .catch((error) => {
                this.loadingBtn = "";
                this.loginBtn = false;
                this.loginState = 0;
                this.$message.error({
                  message: '服务器异常',
                });
              });
          });
        }
      });
    },
    click(_this, data) {
      if (Object.keys(data).length > 0) {
        this.loadingBtn = "登录成功";
        this.$message.success({
          message: "登录成功",
        });
        _this.errNum = 0;
        this.$store.commit("SAVE_USERINFO", data);
        // 记住密码具体实现
        let obj = {
          username: data.username,
          password: data.password,
          level: data.level,
        };
        obj.password = this.isKeep ? obj.password : "";// 根据是否勾选记住密码决定是否保存密码
        this.$cookies.set(`sms_${data.username}`, obj, 60 * 60 * 24 * 3);// 保存3天
        localStorage.setItem("userToken", data.token);
        localStorage.setItem("refreshToken", data.refreshToken);
        localStorage.setItem("cookiesName", `sms_${data.username}`);
        _this.$router.push("/dashboard");
      } else {
        this.$message.error({
          message: "登录失败，请检查用户名或密码",
        });
        this.loadingBtn = "";
        this.errNum++;
        this.errDone();
      }
    },
    errDone() {
      if (this.errNum > 5) {
        this.time = setTimeout(() => {
          this.errNum = 0;
          this.loginBtn = false;
        }, 5000);
        this.loginBtn = true;
        this.$message.warning({
          message: "连续错误5次，请5秒后重试",
        });
      }
    },
    getCookies() {
      this.dealCookie = [];
      let cookieArr = this.$cookies.keys();
      cookieArr.forEach((data) => {
        if (data.indexOf("sms_") !== -1) {
          this.dealCookie.push(this.$cookies.get(data));
        }
      });
      this.dealCookie = this.dealCookie.filter((data) => {
        return data.level === this.form.level;
      });
    },
    deleteCookies(data) {
      let keyName = `sms_${data.username}`;
      this.$cookies.remove(keyName);
      this.getCookies();
    },
    goToRegister() {
      this.$router.push('/register');
    },
  },
  mounted() {
    this.getCookies();
    this.clearForm(2);
  },
  created() {
    this.loadingBtn = "";
    localStorage.removeItem("cookiesName");
    localStorage.removeItem("userInfo");
    localStorage.removeItem("userToken");
    localStorage.removeItem("refreshToken");
  },
};
</script>

<style scoped>
.form-container {
  padding: 20px;
}

.form {
  margin: 0 auto;
}

.form-icon {
  font-size: 20px;
  color: #409EFF;
  line-height: 40px;
}

.form-input {
  width: 100%;
}

.form-input >>> .el-input__inner {
  height: 40px;
  border-radius: 20px;
  padding-left: 15px;
  transition: all 0.3s;
}

.form-input >>> .el-input__inner:focus {
  border-color: #409EFF;
  box-shadow: 0 0 5px rgba(64, 158, 255, 0.3);
}

.remember-password {
  text-align: right;
  margin-top: -10px;
  margin-bottom: 20px;
}

.login-button {
  width: 100%;
  height: 40px;
  border-radius: 20px;
  font-size: 16px;
  margin-top: 10px;
  transition: all 0.3s;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.3);
}

.cookie-item {
  padding: 10px;
}

.cookie-content {
  display: flex;
  align-items: center;
  padding: 5px;
}

.userIcon {
  font-size: 20px;
  color: #409EFF;
  margin-right: 10px;
}

.cookie-info {
  flex: 1;
}

.username {
  font-weight: bold;
  margin-bottom: 5px;
}

.password-mask {
  display: flex;
  gap: 5px;
}

.circle {
  width: 8px;
  height: 8px;
  background: #999;
  border-radius: 50%;
}

.deleteCook {
  color: #999;
  cursor: pointer;
  transition: all 0.3s;
}

.deleteCook:hover {
  color: #F56C6C;
  transform: scale(1.2);
}

.register-link {
  text-align: center;
  margin-top: -10px;
}

.register-link span {
  color: #666;
  font-size: 14px;
}

.register-link .el-button {
  padding: 0 5px;
  font-size: 14px;
}
</style>
