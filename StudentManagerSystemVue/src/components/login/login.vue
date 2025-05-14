<template>
  <div id="login" :style="note">
    <div class="login-container">
      <div class="login-box">
        <div class="title">
          <h2>成绩可视化管理系统</h2>
          <p>Grade Visualization System</p>
        </div>
        <el-tabs
          type="border-card"
          v-model="tabName"
          @tab-click="tabClick"
          class="login-tabs"
        >
          <el-tab-pane label="学生" name="student">
            <Form class="flip" ref="student_tab"></Form>
          </el-tab-pane>
          <el-tab-pane label="教师" name="teacher">
            <Form ref="teacher_tab"></Form>
          </el-tab-pane>
          <el-tab-pane label="管理员" name="admin">
            <Form ref="admin_tab"></Form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import Form from "./form";
import "../../common/css/star.css";

export default {
  data() {
    return {
      starClass: "star",
      starArr: "",
      keyLabel: "学生",
      tabName: "student",
      loading: false,
      note: {
        backgroundImage: "url(" + require("../../assets/home.jpg") + ")",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundAttachment: "fixed",
      },
      starSty: "",
    };
  },
  methods: {
    tabClick(data) {
      if (data.label !== this.keyLabel) {
        this.flip();
        this.keyLabel = data.label;
      }
      if (data.label === "学生") {
        this.$refs.student_tab.clearForm(2);
        this.$refs.student_tab.getCookies();
      } else if (data.label === "教师") {
        this.$refs.teacher_tab.clearForm(1);
        this.$refs.teacher_tab.getCookies();
      } else if (data.label === "管理员") {
        this.$refs.admin_tab.clearForm(0);
        this.$refs.admin_tab.getCookies();
      }
    },
    flip() {
      let flip = document.querySelector(".el-tabs");
      flip.style.transition = "0.3s";
      flip.style.transform = "rotateY(90deg)";
      setTimeout(() => {
        flip.style.transition = "0.2s";
        flip.style.transform = "rotateY(0)";
      }, 240);
    },
  },
  components: {
    Form,
  },
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body,
#app,
.login {
  height: 100%;
  min-height: 100vh;
}

#login {
  height: 100%;
  min-height: 100vh;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  padding: 20px;
}

#login::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  z-index: 1;
}

.login-container {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 1200px;
  display: flex;
  justify-content: center;
}

.login-box {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.title {
  text-align: center;
  margin-bottom: 30px;
}

.title h2 {
  color: #333;
  font-size: clamp(20px, 5vw, 28px);
  margin-bottom: 10px;
}

.title p {
  color: #666;
  font-size: clamp(12px, 3vw, 14px);
}

.login-tabs {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-tabs {
  transform-style: preserve-3d;
  -webkit-perspective: 1000px;
  transition: 0.8s;
}

.el-tabs__item {
  font-size: clamp(14px, 3vw, 16px);
  padding: 0 20px;
  height: 50px;
  line-height: 50px;
}

.el-tabs__item.is-active {
  color: #409EFF;
  font-weight: bold;
}

.el-tabs__active-bar {
  background-color: #409EFF;
  height: 3px;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .login-box {
    padding: 20px;
  }

  .title {
    margin-bottom: 20px;
  }

  .el-tabs__item {
    padding: 0 15px;
  }
}

@media screen and (max-width: 480px) {
  #login {
    padding: 10px;
  }

  .login-box {
    padding: 15px;
  }

  .el-tabs__item {
    padding: 0 10px;
  }
}
</style>
