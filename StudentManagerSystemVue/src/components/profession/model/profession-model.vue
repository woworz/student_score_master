<template>
  <el-dialog v-dialogDrag :title="title" :visible.sync="dialog" :close-on-click-modal=false width="500px">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="专业名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入专业名称"></el-input>
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
    name: "profession-model",
    data () {
      return {
        dialog: false,
        title: '',
        type: '',
        doneNum: 0,
        form: {
          id: '',
          name: '',
        },
        rules: {
          name: [
            { required: true, message: '请输入专业名称', trigger: 'blur' },
            { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (params) {
        this.type = params.type;
        this.dialog = true;
        this.doneNum = 0;
        if (this.type === 'add') {
          this.title = '新增专业';
          this.form = {
            id: '',
            name: '',
          };
        } else if (this.type === 'edit') {
          this.title = '编辑专业';
          this.form = {
            id: params.row.id,
            name: params.row.name,
          };
        }
      },
      click (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (this.type === 'add') {
              this.addMethod();
            } else if (this.type === 'edit') {
              this.editMethod();
            }
          } else {
            return false;
          }
        })
      },
      addMethod () {
        ++this.doneNum;
        if (this.doneNum === 1) {
          this.add();
        }
      },
      add () {
        this.axiosHelper.post(
          '/api/sms/profession', this.form).then(() => {
          this.doneNum = 0;
          this.$message.success({
            message: '新增成功'
          });
          this.dialog = false;
          this.$emit('search')
        }).catch(() => {
          this.doneNum = 0;
          this.$message.error({
            message: '新增失败'
          });
          this.dialog = false;
        });
      },
      editMethod () {
        ++this.doneNum;
        if (this.doneNum === 1) {
          this.edit();
        }
      },
      edit () {
        this.axiosHelper.put(
          '/api/sms/profession', this.form).then(() => {
          this.doneNum = 0;
          this.$message.success({
            message: '修改成功'
          });
          this.dialog = false;
          this.$emit('search')
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
