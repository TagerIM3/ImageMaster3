<template>
  <div style="padding-top: 3%;height:100%;float: left">
    <Card style="width:100%;height:100%">
      <p slot="title">已提交任务</p>

      <div v-show="outer_flag ===0">
        <RadioGroup v-model="dataMode" type="button" @on-change="changeMode">
          <Radio label="未结束"></Radio>
          <Radio label="已结束"></Radio>
        </RadioGroup>

        <div v-if="flag === 0">
          <Table :loading="loading" :data="show_data" :columns="tableColumns1" stripe></Table>
          <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
              <Page :current="1" @on-change="changePage" page-size="5" :total="data_length"></Page>
            </div>
          </div>
        </div>

        <div v-if="flag === 1">
          <Table :loading="loading" :data="show_data" :columns="tableColumns2" stripe></Table>
          <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
              <Page :current="1" @on-change="changePage" page-size="5" :total="data_length"></Page>
            </div>
          </div>
        </div>
      </div>

      <div v-show="outer_flag === 1">
        <a @click="showTasks">返回<<<</a>
        <div style="width:1000px;height: 500px">
          <canvasForWorker ref="canvasForWorker" ></canvasForWorker>
        </div>
      </div>

    </Card>
  </div>
</template>

<script>
  import workerAPI from '../../api/worker'
  import canvasForWorker from '../util/canvasForWorker'

  export default {
    name: "finishTasks",
    components:{canvasForWorker},
    data() {
      return {
        dataMode: '未结束',
        flag: 0,
        outer_flag: 0,
        indexOfPage: 1,
        indexOfTask: 0,
        loading: true,
        data_length: 0,
        data: [],
        show_data: [],
        tableColumns1: [
          {
            title: '任务名',
            key: 'taskName',
          },
          {
            title: '图片数量',
            key: 'imageNum'
          },
          {
            title: '花费时间(分钟)',
            key: 'minute',
          },
          {
            title: '积分奖励',
            key: 'reward',
          },
          {
            title: '查看详情',
            key: 'action',
            // width: 150,
            align: 'center',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                  },
                  on: {
                    click: () => {
                      this.viewTask(params.index);
                    }
                  }
                }, '进入')
              ]);
            }
          }
        ],

        tableColumns2: [
          {
            title: '任务名',
            key: 'taskName',
          },
          {
            title: '图片数量',
            key: 'imageNum'
          },
          {
            title: '花费时间(分钟)',
            key: 'minute',
          },
          {
            title: '准确率',
            key: 'accuracy'
          },
          {
            title: '积分奖励',
            key: 'reward',
          },
          {
            title: '查看详情',
            key: 'action',
            // width: 150,
            align: 'center',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                  },
                  on: {
                    click: () => {
                      this.viewTask(params.index);
                    }
                  }
                }, '进入')
              ]);
            }
          }
        ]
      }
    },

    mounted() {
      this.showTasks();
    },
    methods: {
      changeMode() {
        if (this.dataMode === '已结束') {
          this.loading = true;
          this.showFinishTaskList();
          this.flag = 1;

        }
        else {
          this.loading = true;
          this.showUnfinishTaskList();
          this.flag = 0;


        }
      },
      showTasks() {
        this.outer_flag = 0;
        if (this.dataMode === '未结束') {
          this.flag = 0;
          this.showUnfinishTaskList();

        } else {
          this.flag = 1;
          this.showFinishTaskList();

        }
      },
      showUnfinishTaskList() {
        workerAPI.getSubmitWorks((res) => {
          console.log(res);
            if (res.message === 'SUCCESS') {
              this.data = res.object;
              this.data_length = this.data.length;
              // this.loading = false;
              for (let i = 0; i < res.object.length; i++) {
                this.data[i]['imageNum'] = this.data[i].images.length;
                this.data[i].minute = this.data[i].minute.toFixed(2);
              }
              this.changePage(this.indexOfPage);

            }
            else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else {
              this.$Message.error('网络错误，请稍后再试');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          });
      },

      showFinishTaskList() {
        workerAPI.getFinishWorks((res) => {
            if (res.message === 'SUCCESS') {
              this.data = res.object;
              this.data_length = this.data.length;

              for (let i = 0; i < res.object.length; i++) {
                this.data[i]['imageNum'] = this.data[i].images.length;
                this.data[i].minute = this.data[i].minute.toFixed(2);
              }
              this.changePage(this.indexOfPage);
            }
            else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else {
              this.$Message.error('网络错误，请稍后再试');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          })
      },

      changePage(pageSize) {
        this.indexOfPage = pageSize;
        let loc = pageSize * 5;
        let i;
        this.show_data = [];
        if (loc > this.data.length) {
          for (i = 0; i < this.data.length - loc + 5; i++) {
            this.show_data[i] = this.data[loc - 5 + i];
          }
        }
        else {
          for (i = 0; i < 5; i++) {
            this.show_data[i] = this.data[loc - 5 + i];
          }
        }
        this.loading = false;

      },

      viewTask(index) {
        this.indexOfTask = index;
        //加载展示已完成任务......
        console.log(this.$refs.canvasForWorker);
        this.outer_flag = 1;
        this.$refs.canvasForWorker.load_pic(this.show_data[index], 0);
      }

    }


  }
</script>

<style scoped>

</style>
