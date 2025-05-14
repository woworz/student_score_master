<template>
  <el-card style="margin: 10px;padding: 15px 10px 10px 10px">
    <el-row style="margin-bottom: 10px">
      <el-button type="primary" size="small" @click="addMethod">新增</el-button>
      <el-button type="danger" size="small" @click="deleteSelect">删除</el-button>
      <el-input style="float:right;width:300px;" size="small" v-model="searchValue.code" placeholder="请输入课程名或专业" clearable @keyup.enter.native="filterData">
        <el-button slot="append" @click="filterData" type="primary">过滤</el-button>
      </el-input>
    </el-row>
    <VmBaseTable
      :setTableHigh="true"
      @on-select-change="select"
      ref="course_table"
      :data="dataTable"
      :columns="dataColumns"
      :overflow="false"
      @page-change="pageChange"
      showCheck
      showIndex
      :tableHigh="tableHigh"
    ></VmBaseTable>
    <VmCourse ref="course_model" @search="search"></VmCourse>
    <VmCourseInfo ref="course_info_model" @search="search"></VmCourseInfo>
  </el-card>
</template>

<script>
  import VmCourse from './model/course-model'
  import VmCourseInfo from './model/course-info-model'
  import VmBaseTable from '../../base/base-table'
  export default {
    name: "student",
    components: {
      VmBaseTable, VmCourse, VmCourseInfo
    },
    data () {
      return {
        right: true,
        table: null,
        searchValue: {
          $limit: 10,
          $offset: 0,
          code: ''
        },
        tableHigh: '66vh',
        selectValue: [],
        dataTable: [],
        dataColumns: [
          {
            label: '课程名',
            prop: 'name',
            style: 'center',
            minWidth: '120'
          }, {
            label: '学分',
            prop: 'credits',
            style: 'center',
            minWidth: '60',
            render (h, params) {
              if (params.row.credits % 1 === 0) {
                return h('div', {}, `${params.row.credits}.0`)
              } else {
                return h('div', {}, params.row.credits)
              }
            }
          }, {
            label: '课时',
            prop: 'number',
            style: 'center',
            minWidth: '60',
          }, {
            label: '满分',
            prop: 'score',
            style: 'center',
            minWidth: '60',
          }, {
            label: '类型',
            prop: 'type',
            style: 'center',
            minWidth: '70',
            render (h, params) {
              if (params.row.type === 1) {
                return h('div', {}, '必修')
              } else if (params.row.type === 2) {
                return h('div', {}, '选修')
              }
            }
          }, {
            label: '专业',
            prop: 'profession',
            style: 'center',
            minWidth: '120',
          }, {
            label: '届时',
            prop: 'year',
            style: 'center',
            minWidth: '100',
            render (h, params) {
              return h('div', {}, `${params.row.year}届`)
            }
          }, {
            label: '学期',
            prop: 'term',
            style: 'center',
            minWidth: '100',
            render (h, params) {
              if (params.row.term === 1) {
                return h('div', {}, '上学期')
              } else if (params.row.term === 2) {
                return h('div', {}, '下学期')
              }
            }
          }, {
            label: '操作',
            style: 'center',
            prop: 'id',
            minWidth: '250',
            render: (h, params) => {
              let btns = [];
              btns.push(this.getOpBtn(h, '编辑', 'primary', () => {
                this.editMethod(params.row)
              }));
              btns.push(this.getOpBtn(h, '编辑详情', 'success', () => {
                this.editInfoMethod(params.row)
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
          '/api/sms/course/getCourseList',
          {params: that.searchValue}
        ).then(response => {
          this.dataTable = response.data.items;
          this.table.total = response.data.totalCount
        }).catch(error => {
          this.$message.error({
            message: '失败'
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
        if (this.$refs['course_table'] !== undefined) {
          this.$refs['course_table'].currentPageToOne();
        }
        this.search()
      },
      addMethod() {
        this.search();
        let type = 'add';
        let params = {
          type
        };
        this.$refs['course_model'].init(params);
      },
      editMethod(row) {
        let data = Object.assign({}, row);
        let type = 'edit';
        let params = {
          type,
          row: data
        };
        this.$refs['course_model'].init(params);
      },
      editInfoMethod(row) {
        let params = {
          row
        };
        this.$refs['course_info_model'].init(params);
      },
      select(selection) {
        this.delBtn = selection.length <= 0;
      },
      deleteSingle (obj) {
        let ids = [obj.id];
        this.deleteTable(ids);
      },
      deleteSelect () {
        let ids = this.table.getIds();
        if (ids.length > 0) {
          this.deleteTable(ids);
        }
      },
      deleteTable(ids) {
        let _this = this;
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteMethod(ids, _this);
        }).catch(() => {
          this.$message.info({
            message: '已取消删除'
          });
        });
      },
      deleteMethod(params, _this) {
        console.log(params)

        _this.axiosHelper.delete(
          '/api/sms/course/' + params
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
      },
      // 自定义操作按钮，减少按钮间距
      getOpBtn(h, text, type, func) {
        return h('el-button', {
          props: {
            size: 'mini',
            type: type
          },
          style: {
            marginRight: '5px',
            padding: '4px 8px'
          },
          on: {
            click: func
          }
        }, text)
      }
    },
    mounted () {
      this.table = this.$refs['course_table'];
      this.search();
    }
  }

</script>
