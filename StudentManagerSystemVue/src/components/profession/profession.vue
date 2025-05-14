<template>
  <el-card style="margin: 10px;padding: 15px 10px 10px 10px">
    <el-row style="margin-bottom: 10px">
      <el-button type="primary" size="small" @click="addMethod">新增</el-button>
      <el-button type="danger" size="small" @click="deleteSelect" :disabled="delBtn">删除</el-button>
      <el-input style="float:right;width:300px;" size="small" v-model="searchValue.name" placeholder="请输入专业名称" clearable @keyup.enter.native="filterData">
        <el-button slot="append" @click="filterData" type="primary">过滤</el-button>
      </el-input>
    </el-row>
    <VmBaseTable
      :setTableHigh="true"
      @on-select-change="select"
      ref="profession_table"
      :data="dataTable"
      :columns="dataColumns"
      @page-change="pageChange"
      showCheck
      :tableHigh="tableHigh"
    ></VmBaseTable>
    <VmProfession ref="profession_model" @search="search"></VmProfession>
  </el-card>
</template>

<script>
  import VmProfession from './model/profession-model'
  import VmBaseTable from '../../base/base-table'
  export default {
    name: "profession",
    components: {
      VmBaseTable, VmProfession
    },
    data () {
      return {
        table: null,
        searchValue: {
          $limit: 10,
          $offset: 0,
          name: ''
        },
        tableHigh: '66vh',
        selectValue: [],
        dataTable: [],
        dataColumns: [
          {
            label: 'ID',
            prop: 'id',
            style: 'center',
            minWidth: '80',
          } , {
            label: '专业名称',
            prop: 'name',
            style: 'center',
            minWidth: '200',
          } , {
            label: '操作',
            style: 'center',
            minWidth: '150',
            render: (h, params) => {
              let btns = [];
              btns.push(this.getOpBtn(h, '编辑', 'primary', () => {
                this.editMethod(params.row)
              }));
              btns.push(this.getOpBtn(h, '删除', 'danger', () => {
                this.deleteSingle(params.row)
              }));
              return h('div', btns)
            }
          }
        ],
        delBtn: true
      }
    },
    methods: {
      search() {
        let that = this;
        this.axiosHelper.get(
          '/api/sms/profession',
          {params: that.searchValue}
        ).then(response => {
          this.dataTable = response.data.items;
          this.table.total = response.data.totalCount
        }).catch(error => {
          this.$message.error({
            message: '获取专业信息失败'
          }, error)
        })
      },
      pageChange(page) {
        this.searchValue.$limit = page.limit;
        this.searchValue.$offset = page.offset;
        this.search();
      },
      filterData() {
        this.searchValue.$offset = 0;
        // 跳转到第一页
        if (this.$refs['profession_table'] !== undefined) {
          this.$refs['profession_table'].currentPageToOne();
        }
        this.search()
      },
      addMethod() {
        this.search();
        let type = 'add';
        let params = {
          type
        };
        this.$refs['profession_model'].init(params);
      },
      editMethod(row) {
        let type = 'edit';
        let params = {
          type,
          row
        };
        this.$refs['profession_model'].init(params);
      },
      select(selection) {
        this.selectValue = selection;
        this.delBtn = selection.length <= 0;
      },
      deleteSingle(row) {
        this.$confirm('确认删除该专业?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteMethod([row.id], this);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      deleteSelect() {
        if (this.selectValue.length > 0) {
          this.$confirm('确认删除所选专业?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ids = [];
            for (let i = 0; i < this.selectValue.length; i++) {
              ids.push(this.selectValue[i].id);
            }
            this.deleteMethod(ids, this);
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
        } else {
          this.$message.warning({
            message: '请先选择专业'
          })
        }
      },
      getOpBtn(h, text, type, func) {
        return h('el-button', {
          props: {
            size: 'mini',
            type: type
          },
          style: {
            marginRight: '10px'
          },
          on: {
            click: func
          }
        }, text)
      },
      deleteMethod(params, _this) {
        _this.axiosHelper.delete(
          '/api/sms/profession/' + params
        ).then(response => {
          let status = response.status;
          if (status === 200) {
            // 若删除当前页的最后一条时，加载上一页信息
            if ((_this.table.total - params.length) % 10 === 0 && _this.searchValue.$offset !== 0) {
              _this.searchValue.$offset -= 10
            }
            _this.$message.success({
              message: '删除成功'
            });
            _this.search()
          }
        }).catch(error => {
          _this.$message.error({
            message: '删除失败'
          }, error)
        })
      }
    },
    mounted () {
      this.table = this.$refs['profession_table'];
      this.search();
    }
  }
</script>
