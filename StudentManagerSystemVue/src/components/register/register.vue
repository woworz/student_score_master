<template>
  <div id="register" :style="note">
    <div class="register-container">
      <div class="register-box">
        <div class="title">
          <h2>成绩可视化管理系统</h2>
          <p>Grade Visualization System</p>
          <h3>用户注册</h3>
        </div>
        <el-form
          :model="form"
          :rules="rules"
          ref="form"
          label-width="100px"
          class="register-form"
        >
          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              @blur="checkUsername"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="passwordAgain">
            <el-input
              v-model="form.passwordAgain"
              type="password"
              placeholder="请再次输入密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="真实姓名" prop="realName">
            <el-input
              v-model="form.realName"
              placeholder="请输入真实姓名"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户角色" prop="level">
            <el-select v-model="form.level" placeholder="请选择用户角色">
              <el-option :value="0" label="管理员"></el-option>
              <el-option :value="1" label="教师"></el-option>
              <el-option :value="2" label="学生"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="学校" prop="school">
            <el-input
              v-model="form.school"
              placeholder="请输入学校名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="入学时间" prop="admissionTime" v-if="form.level === 2">
            <el-date-picker
              v-model="form.admissionTime"
              type="year"
              placeholder="选择入学年份"
              value-format="yyyy"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="电子邮箱" prop="email">
            <el-input
              v-model="form.email"
              placeholder="请输入电子邮箱"
            ></el-input>
          </el-form-item>
          <el-form-item label="专业" prop="profession" v-if="form.level !== 0">
            <el-input
              v-model="form.profession"
              placeholder="请输入专业"
            ></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="phone">
            <el-input
              v-model="form.phone"
              placeholder="请输入联系电话"
            ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <el-radio-group v-model="form.sex">
              <el-radio :label="0">男</el-radio>
              <el-radio :label="1">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年级" prop="grade" v-if="form.level === 2">
            <el-input
              v-model="form.grade"
              placeholder="请输入年级"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="submitForm('form')"
              :loading="loading"
            >
              注册
            </el-button>
            <el-button @click="resetForm('form')">重置</el-button>
            <el-button @click="goToLogin">返回登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    // 密码验证
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.form.passwordAgain !== "") {
          this.$refs.form.validateField("passwordAgain");
        }
        callback();
      }
    };
    // 确认密码验证
    const validatePassAgain = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.form.password) {
        callback(new Error("两次输入密码不一致"));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      form: {
        username: "",
        password: "",
        passwordAgain: "",
        realName: "",
        level: 2,
        school: "",
        admissionTime: "",
        email: "",
        profession: "",
        phone: "",
        sex: 0,
        grade: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 20, message: "长度在 3 到 20 个字符", trigger: "blur" },
        ],
        password: [
          { required: true, validator: validatePass, trigger: "blur" },
          { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" },
        ],
        passwordAgain: [
          { required: true, validator: validatePassAgain, trigger: "blur" },
        ],
        realName: [
          { required: true, message: "请输入真实姓名", trigger: "blur" },
        ],
        level: [
          { required: true, message: "请选择用户角色", trigger: "change" },
        ],
        school: [
          { required: true, message: "请输入学校名称", trigger: "blur" },
        ],
        email: [
          { required: true, message: "请输入电子邮箱", trigger: "blur" },
          { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" },
        ],
        phone: [
          { required: true, message: "请输入联系电话", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" },
        ],
        sex: [
          { required: true, message: "请选择性别", trigger: "change" },
        ],
      },
      note: {
        backgroundImage: "url(" + require("../../assets/home.jpg") + ")",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundAttachment: "fixed",
      },
    };
  },
  methods: {
    // 检查用户名是否已存在
    checkUsername() {
      if (this.form.username && this.form.username.length >= 3) {
        this.axiosHelper
          .get(`/api/sms/user/register/check/${this.form.username}`)
          .then((response) => {
            if (response.data.exists) {
              this.$message.error("用户名已存在，请更换其他用户名");
              this.form.username = "";
            }
          })
          .catch((error) => {
            console.error("检查用户名失败", error);
          });
      }
    },
    // 提交表单
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.loading = true;
          // 设置ID等于用户名
          this.form.id = this.form.username;

          this.axiosHelper
            .post("/api/sms/user/register", this.form)
            .then((response) => {
              this.loading = false;
              if (response.data.success) {
                this.$message.success(response.data.message);
                setTimeout(() => {
                  this.goToLogin();
                }, 1500);
              } else {
                this.$message.error(response.data.message);
              }
            })
            .catch((error) => {
              this.loading = false;
              this.$message.error("注册失败，请稍后重试");
              console.error("注册失败", error);
            });
        } else {
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 返回登录页
    goToLogin() {
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#register {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-container {
  width: 500px;
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  padding: 20px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.title h2 {
  font-size: 24px;
  color: #409eff;
  margin-bottom: 5px;
}

.title p {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
}

.title h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 10px;
}

.register-form {
  padding: 0 20px;
}

.el-form-item {
  margin-bottom: 15px;
}
</style>
