<template>
  <div>
    <!--个人信息-->
    <el-card class="content" style="width: 55%;margin: 12px">
      <div class="title">
        <i class="el-icon-user-solid"></i> 个人信息
        <div style="float: right">
          <el-link :underline="false" class="edit" @click="editInfo">修改资料</el-link>
        </div>
      </div>
      <el-row style="color: #666666;padding: 20px">
        <el-col :span="6" style="margin-left: 10px;margin-top: 10px" class="upload" v-loading="loading">
          <el-upload
            action=""
            :http-request="submitUpload"
            :on-change="onchange"
            :show-file-list="false"
            accept=".png,.jpg"
          >
            <div class="header">
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
        <el-col :span="14" style="margin-left: 50px">
          <h2 class="distance" style="margin-bottom: 35px;margin-left: 15px">{{userInfo.realName}}</h2>
          <div class="distance">
            <i class="el-icon-male" v-if="userInfo.sex === 0" title="性别"> {{sexName}}</i>
            <i class="el-icon-female" v-else title="性别"> {{sexName}}</i>{{'&#12288'}}{{'&#12288'}}
            <i class="el-icon-office-building" title="学校"> {{userInfo.school}}</i>
          </div>
          <div class="distance">
            <i class="el-icon-phone-outline" title="联系方式"> {{userInfo.phone}}</i> {{'&#12288&#12288'}}
            <i class="el-icon-message" title="邮箱"> {{userInfo.email}}</i>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!--饼状图-->
    <el-card class="content" style="width: 36.8%;margin: 12px 12px 12px 0">
      <el-button type="text" icon="el-icon-s-tools" @click="setting" class="settingStyle" title="选择年级"></el-button>
      <div style="margin-top: -15px" v-if="showPie">
        <ve-pie :data="pieData" :settings="pieSetting"></ve-pie>
      </div>
      <div v-else style="text-align: center;line-height: 200px">
        <span style="color: gray">暂无数据</span>
      </div>
    </el-card>
    <!--折线图-->
    <el-card class="content" style="width: 98%;margin: 0 0 12px 12px">
      <el-button v-if="showChart" title="切换图表" @click="changeMethod" class="changeChartStyle" type="text" icon="el-icon-refresh"></el-button>
      <div style="margin-top: -15px" v-if="showChart">
        <ve-line v-if="changeChart" :data="lineData" :width="lineWidth" ref="chart" :height="lineHeight"></ve-line>
        <ve-histogram :data="histogramData" :height="lineHeight" ref="chart" v-else></ve-histogram>
      </div>
      <div style="text-align: center;line-height: 290px" v-else>
        <span style="color: gray">暂无数据</span>
      </div>
    </el-card>
    <editInfo ref="editInfo_model" @refresh="refresh"></editInfo>
    <adminSetting ref="setting_model" @professionInfo="professionInfo"></adminSetting>
    <VmUpload ref="upload_model" @propUrl="propUrl"></VmUpload>
  </div>
</template>

<script>
import adminSetting from './model/admin-setting-model'
import VmUpload from './model/upload-model'
import editInfo from './model/admin-edit-model'
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
      loading: false,
      form: null,
      imageUrl: '',
      showChart: false,
      showPie: false,
      lineHeight: '330px',
      lineWidth: '100%',
      changeChart: true,
      userInfo: {},
      sexName: '男',
      schoolInfo: '',
      myselfInfo: '',
      pieData: {
        columns: ['label', 'value'],
        rows: [],
      },
      histogramData: {
        columns: ['realName', '分数'],
        rows: []
      },
      lineData: {
        columns: ['realName', '分数'],
        rows: []
      }
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
    getChartData (data) {
      this.axiosHelper.get(
        '/api/sms/score/export',
        {params: data}).then(response => {
        let data = response.data;
        this.lineData.rows = data.map(item => {
          let obj = {
            realName: item.realName,
            '分数': item.scoreByUser || 0
          };
          return obj
        });
        this.showChart = data.some(item => {
          return item.scoreByUser !== null
        });
        this.histogramData = this.lineData;
      }).catch(error => {
        this.$message.error({
          message: '获取成绩失败'
        }, error)
      })
    },
    setting () {
      this.$refs.setting_model.init();
    },
    changeMethod () {
      this.changeChart = !this.changeChart;
    },
    getPieData (data) {
      this.axiosHelper.get(
        '/api/sms/score/getUserNum', {params: data}).then(
        response => {
          let data = response.data;
          data = data.filter(item => {
            let flag = true;
            if (item.label === '未录入') {
              flag = item.value !== 0
            }
            return flag
          });
          this.pieData.rows = data;
          this.showPie = data.some(item => {
            return item.value !== 0;
          });
        }).catch(error => {
        this.$message.error({
          message: '获取饼图信息失败'
        }, error)
      })
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
  },
  components: {
    editInfo, adminSetting, VmUpload
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

.admin-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
}

.stat-card {
  flex: 1;
  min-width: 200px;
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px 0 rgba(0,0,0,0.2);
}

.stat-title {
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
}

.stat-value {
  color: #303133;
  font-size: 24px;
  font-weight: 500;
}

.stat-icon {
  font-size: 24px;
  color: #409EFF;
  margin-right: 10px;
}
</style>
<style>
#app , body {
  padding: 0;
  margin: 0;
}
</style>
