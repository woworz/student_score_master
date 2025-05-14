<template>
  <el-dialog v-dialogDrag title="设置" :visible.sync="dialog" :close-on-click-modal=false append-to-body width="500px">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="学年：">
        <el-select v-model="form.year" style="width: 90%">
          <el-option v-for="item in yearArr" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="学期：">
        <el-select v-model="form.term" style="width: 90%">
          <el-option v-for="item in termArr" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button type="primary" @click="click" size="small">确定</el-button>
      <el-button @click="cancel" size="small" >取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "teacher-setting-model",
    data () {
      return {
        dialog: false,
        userInfo: {},
        yearArr: [],
        form: {
          term: '',
          year: ''
        },
        termArr: [{
          label: '上',
          value: 1
        },{
          label: '下',
          value: 2
        }],
      }
    },
    methods: {
      init () {
        this.dialog = true;
      },
      getYear () {
        let now = new Date().getFullYear();
        let old = parseInt(this.userInfo.admissionTime);
        // 最多显示4个学年选项（从入学年份开始）
        for (let i = old; i < now; i++) {
          if (this.yearArr.length < 4) {
            // 使用自然年份作为标签
            let obj = {label: i.toString(), value: i};
            this.yearArr.push(obj);
          }
        }
        this.getDefault();
      },
      getDefault () {
        // 获取学年和学期的初始值
        // 获取用户信息
        let userInfo = JSON.parse(localStorage.userInfo);
        // 获取用户入学时间并设置为默认学年
        this.form.year = parseInt(userInfo.admissionTime);
        this.form.term = this.termArr[0].value;
        this.click();
      },
      click () {
        this.$emit('studentInfo', this.form);
        this.dialog = false;
      },
      cancel () {
        this.dialog = false;
      }
    },
    mounted() {
      this.userInfo = JSON.parse(localStorage.userInfo);
      this.getYear();
    },
  }
</script>

<style scoped>

</style>
