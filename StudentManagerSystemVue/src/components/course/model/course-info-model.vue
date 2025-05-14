<template>
  <el-dialog v-dialogDrag :title="title" :visible.sync="dialog" :close-on-click-modal=false width="500px">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="课程名称">
        <el-input v-model="form.name" disabled></el-input>
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="form.profession" disabled></el-input>
      </el-form-item>
      <el-form-item label="开始周数" prop="start">
        <el-input-number v-model="form.start" :min="1" :max="25" size="small"></el-input-number>
      </el-form-item>
      <el-form-item label="结束周数" prop="end">
        <el-input-number v-model="form.end" :min="1" :max="25" size="small"></el-input-number>
      </el-form-item>
      <el-form-item label="教室" prop="room">
        <el-input v-model="form.room" placeholder="请输入教室"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button type="primary" @click="click('form')" size="small">确定</el-button>
      <el-button @click="cancel" size="small">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "course-info-model",
    data () {
      const validateEnd = (rule, value, callback) => {
        if (value < this.form.start) {
          callback(new Error('结束周数必须大于等于开始周数'));
        } else {
          callback();
        }
      };
      return {
        dialog: false,
        title: '编辑课程详情',
        doneNum: 0,
        form: {
          id: '',
          courseId: '',
          name: '',
          profession: '',
          start: 1,
          end: 25,
          room: ''
        },
        rules: {
          start: [
            { required: true, message: '请输入开始周数', trigger: 'blur' },
            { type: 'number', min: 1, max: 25, message: '周数范围在 1 到 25 之间', trigger: 'blur' }
          ],
          end: [
            { required: true, message: '请输入结束周数', trigger: 'blur' },
            { type: 'number', min: 1, max: 25, message: '周数范围在 1 到 25 之间', trigger: 'blur' },
            { validator: validateEnd, trigger: 'blur' }
          ],
          room: [
            { required: true, message: '请输入教室', trigger: 'blur' },
            { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (params) {
        this.dialog = true;
        this.doneNum = 0;
        this.title = '编辑课程详情';
        
        // 获取课程详情信息
        this.axiosHelper.get('/api/sms/course/info/' + params.row.id).then(response => {
          const courseInfo = response.data;
          this.form = {
            id: courseInfo.id || '',
            courseId: params.row.id,
            name: params.row.name,
            profession: params.row.profession,
            start: courseInfo.start || 1,
            end: courseInfo.end || 25,
            room: courseInfo.room || ''
          };
        }).catch(error => {
          console.error('获取课程详情失败', error);
          this.$message.error('获取课程详情失败');
          this.dialog = false;
        });
      },
      click (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.editMethod();
          } else {
            return false;
          }
        })
      },
      editMethod () {
        ++this.doneNum;
        if (this.doneNum === 1) {
          this.edit();
        }
      },
      edit () {
        this.axiosHelper.put('/api/sms/course/info', this.form).then(() => {
          this.doneNum = 0;
          this.$message.success({
            message: '修改成功'
          });
          this.dialog = false;
          this.$emit('search');
        }).catch(() => {
          this.doneNum = 0;
          this.$message.error({
            message: '修改失败'
          });
          this.dialog = false;
        });
      },
      cancel () {
        this.dialog = false;
      }
    }
  }
</script>
