<!--教师首页-->
<template>
  <div style="">
    <!--个人信息-->
    <el-card class="content" style="width: 55%;margin: 12px">
      <div class="title">
        <i class="el-icon-user-solid"></i> 个人信息
        <div style="float: right">
          <el-link :underline="false" class="edit" @click="editInfo">修改资料</el-link>
        </div>
      </div>
      <!-- 个人信息内容区域-->
      <el-row style="color: #666666;padding: 15px">
        <!--头像上传-->
        <el-col :span="6" style="margin-left: 10px;margin-top: 10px" class="upload" v-loading="loading">
          <el-upload
            action=""
            :http-request="submitUpload"
            :on-change="onchange"
            :show-file-list="false"
            accept=".png,.jpg"
          >
            <div class="header">
              <!--显示头像-->
              <el-image :src="$store.state.baseApi + imageUrl" class="header imgStyle">
                <div slot="error" class="image-slot">
                  <div style="width: 120px; height: 140px">
                    <i class="el-icon-user-solid"></i>
                  </div>
                </div>
              </el-image>
            </div>
            <div class="camera" @click="submitUpload"><i class="el-icon-camera"></i></div>
          </el-upload>
        </el-col>
        <!--详细信息-->
        <el-col :span="17" style="margin-left: 10px">
          <h2 class="distance" style="margin-bottom: 35px;margin-left: 15px">{{userInfo.realName}}</h2>
          <div class="distance">
            <i class="el-icon-user" title="编号"> {{userInfo.id}}</i>{{'&#12288'}}
            <i class="el-icon-male" v-if="userInfo.sex === 0" title="性别"> {{sexName}}</i>
            <i class="el-icon-female" v-else title="性别"> {{sexName}}</i>{{'&#12288'}}
            <i class="el-icon-office-building" title="学校"> {{userInfo.school}}</i>{{'&#12288'}}
            <a class="courseInfo el-icon-reading" @click="courseClick"> 任课信息</a>
          </div>
          <div class="distance">
            <i class="el-icon-phone-outline" title="联系方式"> {{userInfo.phone}}</i> {{'&#12288&#12288'}}
            <i class="el-icon-message" title="邮箱"> {{userInfo.email}}</i>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!--任课信息-->
    <el-card class="content" style="width: 98%;margin: 0 0 12px 12px">
      <div class="title">
        <i class="el-icon-reading"></i> 任课信息
        <div style="float: right">
          <el-link :underline="false" class="edit" @click="courseClick">查看更多</el-link>
        </div>
      </div>
      <div v-if="teacherCourses.length > 0" style="padding: 15px">
        <div class="course-list">
          <div v-for="(course, index) in teacherCourses" :key="index" class="course-item">
            <div><strong>{{ course.name }}</strong></div>
            <div>{{ course.profession }} {{ course.grade }}</div>
            <div>{{ getTermText(course.term) }}学期</div>
          </div>
        </div>
      </div>
      <div v-else style="text-align: center;line-height: 200px">
        <span style="color: gray">暂无任课信息</span>
      </div>
    </el-card>
    <div>
      <!--<div style="float: right;margin: 20px 150px 0 0">-->
    </div>
    <editInfo ref="editInfo_model" @refresh="refresh"></editInfo>
    <teacherSetting ref="setting_model" @professionInfo="professionInfo"></teacherSetting>
    <courseInfo ref="Vm_courseInfo"></courseInfo>
    <VmUpload ref="upload_model" @propUrl="propUrl"></VmUpload>
  </div>
</template>

<script>
import teacherSetting from './model/teacher-setting-model'
import VmUpload from './model/upload-model'
import editInfo from './model/teacher-edit-model'
import courseInfo from '../teacher/model/courseInfo-model'
export default {
  inject:['reload'],
  name: "dashboard",
  data () {
    this.pieSetting = {
      offsetY: 150,
      radius: 90,
      labelLine: {
        length: 8,
        smooth: false
      },
    };
    return {
      imageUrl: '',
      loading: false,
      showPie: false,
      userInfo: {},
      sexName: '男',
      schoolInfo: '',
      myselfInfo: '',
      pieData: {
        columns: ['label', 'value'],
        rows: [],
      },

      teacherCourses: []
    }
  },
  methods: {
    getSchoolInfo () {
      let userInfo = this.userInfo;
      this.sexName = userInfo.sex === 0 ? '男' : '女';
    },
    editInfo () {
      let userInfo = Object.assign({}, this.userInfo);
      this.$refs.editInfo_model.init(userInfo);
    },
    refresh () {
      // 刷新页面
      this.reload();
    },
    propUrl (url) {
      this.imageUrl = url;
      this.loading = true;
      setTimeout(() => {
        this.loading = false;
      }, 500);
    },
    onchange () {
      const event = event || window.event;
      const data = event.target.files[0];
      const reader = new FileReader();
      //转base64
      reader.onload = e => {
        const imageUrl = e.target.result; //将图片路径赋值给src
        this.$refs.upload_model.init(imageUrl, this.form)
      };
      reader.readAsDataURL(data);
    },
    submitUpload (params) {
      const file = params.file;
      const fileType = file.type;
      const isImage = fileType.indexOf("image") !== -1;
      const isLt2M = file.size / 1024 / 1024 < 2;
      // 这里常规检验，看项目需求而定
      if (!isImage) {
        this.$message.warning({
          message: '只能上传图片格式png、jpg!'
        });
        return;
      }
      if (!isLt2M) {
        this.$message.warning({
          message: '只能上传图片大小小于2M'
        });
        return;
      }
      // 根据后台需求数据格式
      this.form = new FormData();
      // 文件对象
      this.form.append("file", file);
      this.form.append("id", this.userInfo.id);
      this.form.append("level", this.userInfo.level);
    },
    setting () {
      this.$refs.setting_model.init();
    },
    courseClick () {
      this.$refs.Vm_courseInfo.init(this.userInfo);
    },
    changeMethod () {
      this.changeChart = !this.changeChart;
    },
    professionInfo (data) {
      let userInfo = JSON.parse(localStorage.userInfo);
      let obj = {
        username: userInfo.username,
        level: userInfo.level,
        profession: data.profession || '',
        grade: data.grade || '',
        courseName: data.course || ''
      };
      this.getChartData(obj);
      this.getPieData(obj);
    },
    getHead() {
      const obj = {
        userId: this.userInfo.id,
        level: this.userInfo.level
      };
      this.axiosHelper.get(
        '/api/sms/upload/getHeadImg', {params: obj}).then(
        response => {
          this.imageUrl = response.data;
        }).catch(error => {
        this.$message.error({
          message: '获取头像失败'
        }, error)
      })
    },

    // 获取教师任课信息
    getTeacherCourses() {
      if (this.userInfo && this.userInfo.id) {
        this.axiosHelper.get('/api/sms/teacher/course/getCourseListById/' + this.userInfo.id).then(
          response => {
            this.teacherCourses = response.data;
          }).catch(error => {
          this.$message.error({
            message: '获取任课信息失败'
          }, error)
        })
      }
    },

    // 获取学期文本
    getTermText(term) {
      return term === 1 ? '上' : '下';
    }
  },
  computed: {
    collapse() {
      return this.$store.state.collapse;
    }
  },
  watch: {
    collapse() {
      if (this.$refs['chart']) {
        setTimeout(() => {
          this.$refs['chart'].resize()
        }, 150)
      }
    }
  },
  mounted() {
    this.userInfo = JSON.parse(localStorage.userInfo);
    this.getSchoolInfo();
    this.getHead();
    this.getTeacherCourses();
  },
  components: {
    editInfo, teacherSetting, courseInfo, VmUpload
  }
}
</script>

<style scoped>
.header {
  background-color: #fff;
  font-size: 80px;
  color: #409EFF;
  width: 120px;
  border-radius: 8px;
  height: 140px;
  line-height: 140px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.upload:hover .imgStyle{
  opacity: 0.2;
}

.upload:hover .header::after {
  transition: all 0.3s ease;
  content: "修改我的头像";
  font-size: 13px;
  position: absolute;
  right: 20px;
  top: 30px;
  color: #333;
}

.upload {
  position: relative;
  top: 0;
  left: 0;
  width: 120px;
}

.camera {
  color: #409EFF;
  font-size: 35px;
  position: absolute;
  top: 35px;
  right: 42px;
  opacity: 0;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload:hover .camera {
  opacity: 1;
}

.distance {
  margin: 15px 0;
  color: #606266;
  font-size: 14px;
}

.title {
  font-size: 18px;
  background-color: #f8f9fc;
  border-bottom: 1px solid #ebeef5;
  height: 50px;
  line-height: 50px;
  padding: 0 20px;
  color: #409EFF;
  font-weight: 500;
}

.content {
  height: 300px;
  float: left;
  background-color: #fff;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.content:hover {
  background-color: #fff;
  box-shadow: 0 4px 16px 0 rgba(0,0,0,0.2);
  transform: translateY(-5px);
}

.edit:hover {
  transform: scale(1.1);
  color: #409EFF;
}

.settingStyle {
  color: #909399;
  margin-left: 450px;
  transition: all 0.3s ease;
}

.settingStyle:hover {
  transform: scale(1.2);
  color: #409EFF;
}

.changeChartStyle {
  margin: -4px 0 0 97.5%;
  color: #909399;
  font-size: 16px;
  transition: all 0.3s ease;
}

.changeChartStyle:hover {
  transform: scale(1.2);
  color: #409EFF;
}

.courseInfo {
  color: #606266;
  text-decoration: none;
  transition: all 0.3s ease;
}

.courseInfo:hover {
  color: #409EFF;
  cursor: pointer;
  transform: scale(1.05);
}

.user-info {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.user-info h2 {
  color: #303133;
  margin-bottom: 20px;
  font-weight: 500;
}

.user-info i {
  color: #409EFF;
  margin-right: 5px;
}

.chart-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
}

.pie-chart {
  flex: 1;
  min-width: 300px;
}

.line-chart {
  flex: 2;
  min-width: 600px;
}

.course-info-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-top: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.course-info-card h3 {
  color: #303133;
  margin-bottom: 15px;
  font-weight: 500;
}

.course-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.course-item {
  background-color: #f8f9fc;
  padding: 10px 15px;
  border-radius: 4px;
  color: #606266;
  transition: all 0.3s ease;
}

.course-item:hover {
  background-color: #ecf5ff;
  color: #409EFF;
  transform: translateY(-2px);
}
</style>
<style>
#app , body {
  padding: 0;
  margin: 0;
}
</style>
