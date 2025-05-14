<template>
  <el-card style="margin: 10px; padding: 15px 10px 10px 10px">
    <el-row style="margin-bottom: 10px">
      <el-button type="danger" size="small" @click="deleteSelect" :disabled="delBtn">删除</el-button>
      <el-select v-model="searchValue.status" placeholder="请选择状态" size="small" style="margin-left: 10px" @change="filterData">
        <el-option label="全部" :value="null"></el-option>
        <el-option label="待审核" :value="0"></el-option>
        <el-option label="已批准" :value="1"></el-option>
        <el-option label="已拒绝" :value="2"></el-option>
      </el-select>
      <el-select v-model="searchValue.level" placeholder="请选择角色" size="small" style="margin-left: 10px" @change="filterData">
        <el-option label="全部" :value="null"></el-option>
        <el-option label="管理员" :value="0"></el-option>
        <el-option label="教师" :value="1"></el-option>
        <el-option label="学生" :value="2"></el-option>
      </el-select>
      <el-input
        style="float: right; width: 300px"
        size="small"
        v-model="searchValue.code"
        placeholder="请输入编号或姓名"
        clearable
        @keyup.enter.native="filterData"
      >
        <el-button slot="append" @click="filterData" type="primary">过滤</el-button>
      </el-input>
    </el-row>
    <el-table
      :data="dataTable"
      style="width: 100%"
      border
      @selection-change="handleSelectionChange"
      :height="tableHeight"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="编号" width="120"></el-table-column>
      <el-table-column prop="realName" label="姓名" width="100"></el-table-column>
      <el-table-column prop="level" label="角色" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.level === 0">管理员</span>
          <span v-else-if="scope.row.level === 1">教师</span>
          <span v-else-if="scope.row.level === 2">学生</span>
        </template>
      </el-table-column>
      <el-table-column prop="school" label="学校" width="150"></el-table-column>
      <el-table-column prop="profession" label="专业" width="150"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column prop="phone" label="电话" width="120"></el-table-column>
      <el-table-column prop="createTime" label="申请时间" width="180">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">已批准</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="danger">已拒绝</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="success"
            @click="approveRegister(scope.row)"
            >批准</el-button
          >
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="danger"
            @click="rejectRegister(scope.row)"
            >拒绝</el-button
          >
          <el-button
            size="mini"
            type="primary"
            @click="viewDetail(scope.row)"
            >详情</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      style="margin-top: 15px; text-align: right"
    ></el-pagination>

    <!-- 详情对话框 -->
    <el-dialog title="注册申请详情" :visible.sync="dialogVisible" width="600px">
      <el-form :model="detailForm" label-width="100px">
        <el-form-item label="用户名">
          <span>{{ detailForm.username }}</span>
        </el-form-item>
        <el-form-item label="真实姓名">
          <span>{{ detailForm.realName }}</span>
        </el-form-item>
        <el-form-item label="用户角色">
          <span v-if="detailForm.level === 0">管理员</span>
          <span v-else-if="detailForm.level === 1">教师</span>
          <span v-else-if="detailForm.level === 2">学生</span>
        </el-form-item>
        <el-form-item label="学校">
          <span>{{ detailForm.school }}</span>
        </el-form-item>
        <el-form-item label="入学时间" v-if="detailForm.level === 2">
          <span>{{ detailForm.admissionTime }}</span>
        </el-form-item>
        <el-form-item label="电子邮箱">
          <span>{{ detailForm.email }}</span>
        </el-form-item>
        <el-form-item label="专业" v-if="detailForm.level !== 0">
          <span>{{ detailForm.profession }}</span>
        </el-form-item>
        <el-form-item label="联系电话">
          <span>{{ detailForm.phone }}</span>
        </el-form-item>
        <el-form-item label="性别">
          <span>{{ detailForm.sex === 0 ? '男' : '女' }}</span>
        </el-form-item>
        <el-form-item label="班级" v-if="detailForm.level === 2">
          <span>{{ detailForm.grade }}</span>
        </el-form-item>
        <el-form-item label="申请时间">
          <span>{{ formatDate(detailForm.createTime) }}</span>
        </el-form-item>
        <el-form-item label="状态">
          <el-tag v-if="detailForm.status === 0" type="warning">待审核</el-tag>
          <el-tag v-else-if="detailForm.status === 1" type="success">已批准</el-tag>
          <el-tag v-else-if="detailForm.status === 2" type="danger">已拒绝</el-tag>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          v-if="detailForm.status === 0"
          type="success"
          @click="approveRegister(detailForm)"
          >批准</el-button
        >
        <el-button
          v-if="detailForm.status === 0"
          type="danger"
          @click="rejectRegister(detailForm)"
          >拒绝</el-button
        >
        <el-button @click="dialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      searchValue: {
        $limit: 10,
        $offset: 0,
        code: "",
        status: 0, // 默认显示待审核
        level: null,
      },
      dataTable: [],
      multipleSelection: [],
      delBtn: true,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableHeight: "calc(100vh - 230px)",
      dialogVisible: false,
      detailForm: {},
    };
  },
  created() {
    this.getRegisterList();
  },
  methods: {
    // 格式化日期
    formatDate(date) {
      if (!date) return "";
      const d = new Date(date);
      const year = d.getFullYear();
      const month = (d.getMonth() + 1).toString().padStart(2, "0");
      const day = d.getDate().toString().padStart(2, "0");
      const hour = d.getHours().toString().padStart(2, "0");
      const minute = d.getMinutes().toString().padStart(2, "0");
      const second = d.getSeconds().toString().padStart(2, "0");
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    },
    // 获取注册列表
    getRegisterList() {
      this.axiosHelper
        .get("/api/sms/user/register/list", { params: this.searchValue })
        .then((response) => {
          this.dataTable = response.data.items;
          this.total = response.data.totalCount;
        })
        .catch((error) => {
          this.$message.error("获取注册申请列表失败");
          console.error(error);
        });
    },
    // 批准注册
    approveRegister(row) {
      this.$confirm("确定批准该用户的注册申请吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.axiosHelper
            .put(`/api/sms/user/register/approve/${row.id}`)
            .then((response) => {
              if (response.data.success) {
                this.$message.success(response.data.message);
                this.getRegisterList();
                this.dialogVisible = false;
              } else {
                this.$message.error(response.data.message);
              }
            })
            .catch((error) => {
              this.$message.error("批准失败，请稍后重试");
              console.error(error);
            });
        })
        .catch(() => {
          // 取消操作
        });
    },
    // 拒绝注册
    rejectRegister(row) {
      this.$confirm("确定拒绝该用户的注册申请吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.axiosHelper
            .put(`/api/sms/user/register/reject/${row.id}`)
            .then((response) => {
              if (response.data.success) {
                this.$message.success(response.data.message);
                this.getRegisterList();
                this.dialogVisible = false;
              } else {
                this.$message.error(response.data.message);
              }
            })
            .catch((error) => {
              this.$message.error("拒绝失败，请稍后重试");
              console.error(error);
            });
        })
        .catch(() => {
          // 取消操作
        });
    },
    // 查看详情
    viewDetail(row) {
      this.detailForm = { ...row };
      this.dialogVisible = true;
    },
    // 删除选中的注册申请
    deleteSelect() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("请至少选择一条记录");
        return;
      }
      
      this.$confirm("确定删除选中的注册申请吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const ids = this.multipleSelection.map((item) => item.id);
          this.axiosHelper
            .delete(`/api/sms/user/register/${ids.join(",")}`)
            .then((response) => {
              if (response.data.success) {
                this.$message.success(response.data.message);
                this.getRegisterList();
              } else {
                this.$message.error(response.data.message);
              }
            })
            .catch((error) => {
              this.$message.error("删除失败，请稍后重试");
              console.error(error);
            });
        })
        .catch(() => {
          // 取消操作
        });
    },
    // 表格选择变化
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.delBtn = this.multipleSelection.length === 0;
    },
    // 过滤数据
    filterData() {
      this.currentPage = 1;
      this.searchValue.$offset = 0;
      this.getRegisterList();
    },
    // 页码变化
    handleCurrentChange(val) {
      this.currentPage = val;
      this.searchValue.$offset = (val - 1) * this.pageSize;
      this.getRegisterList();
    },
    // 每页条数变化
    handleSizeChange(val) {
      this.pageSize = val;
      this.searchValue.$limit = val;
      this.currentPage = 1;
      this.searchValue.$offset = 0;
      this.getRegisterList();
    },
  },
};
</script>

<style scoped>
.el-pagination {
  margin-top: 15px;
}
</style>
