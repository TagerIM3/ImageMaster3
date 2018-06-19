<template>
  <div>
    <Card style="width:100%;height:100%">
      <p slot="title">我的任务</p>

      <div>
        <div v-show="flag===0" style="min-height: 200px;min-width: 1000px">
          <Table :data="show_data" :columns="tableColumns1" stripe :loading="loading"></Table>
          <div style="margin: 10px;overflow: hidden">
            <div style="float: right;">
              <Page :current="1" @on-change="changePage" page-size="5" :total="data_length"></Page>
            </div>
          </div>
        </div>

        <div v-show="flag===1">
          <a @click="showTasks">返回<<<</a>
          <div style="width:1000px;height: 500px">
            <canvas-for-worker ref="canvasForWorker"></canvas-for-worker>
          </div>
        </div>
      </div>
    </Card>

    <Modal
      v-model="deleteTask"
      title="放弃任务"
      @on-ok="removeTask"
      @on-cancel="">
      <p>确定放弃此任务？</p>
    </Modal>
  </div>


</template>

<script>
  import workerAPI from '../../api/worker'
  import canvasForWorker from '../util/canvasForWorker'

  export default {
    name: "chosenTask",
    components: {canvasForWorker},
    data() {
      return {
        // color: '#19be6b',
        // url: '../../assets/logo.png',
        // imgUrl: require('../../assets/logo.png'),
        // tagModel: '全局标注',
        flag: 0,
        // idList: [],
        // nameList: [],
        // descriptionList: [],
        // bonusList: [],
        // ddl: [],
        deleteTask: false,
        loading: true,
        indexOfPage: 1,
        indexOfTask: 0,
        show_data: [],
        data_length: 0,
        tableColumns1: [
          {
            title: '任务名',
            key: 'taskName',
          },
          {
            title: '任务类型',
            key: 'label',
          },
          {
            title: '图片数量',
            key: 'imageNum',
          },

          {
            title: '积分奖励',
            key: 'reward',
          },

          {
            title: '任务操作',
            key: 'action',
            // width: 150,
            align: 'center',
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    // size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.chooseTask(params.index);
                    }
                  }
                }, '进入'),
                h('Button', {
                  props: {
                    type: 'error',
                    // size: 'small'
                  },
                  on: {
                    click: () => {
                      this.ifDelete(params.index);
                    }
                  }
                }, '删除')


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
      ifDelete(index) {
        this.deleteTask = true;
        this.indexOfTask = index;
      },
      showTasks() {
        this.flag = 0;
        workerAPI.getWorks((res) => {
            if (res.message === 'SUCCESS') {
              this.data = res.object;
              this.data_length = this.data.length;
              this.loading = false;
              for (let i = 0; i < res.object.length; i++) {
                // this.data[i].push({
                //   'imageNum': this.data[i].images.length
                // });
                this.data[i]['imageNum'] = this.data[i].images.length;
              }
              this.changePage(this.indexOfPage);
              // for (var i = 5 * this.indexOfPage - 5; i < 5 * this.indexOfPage && i < works.length; i++) {
              //   var j = i - (5 * this.indexOfPage - 5);
              //   var item = works[i];
              //   this.idList[j] = item.id;
              //   this.nameList[j] = item.taskName;
              //   this.descriptionList[j] = item.description;
              //   this.bonusList[j] = item.reward;
              //   this.ddl[j] = item.finishTime;
              // }
            } else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            } else {
              this.$Message.error('网络错误，请稍后再试');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          });
      },

      changePage(pageSize) {
        this.indexOfTask = pageSize;
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
      },

      removeTask() {

        workerAPI.quitWork(this.show_data[this.indexOfTask].id, (res) => {
            console.log(res);
            if (res === 'SUCCESS') {
              this.showTasks();
            } else if (res === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            } else {
              this.$Message.error('网络错误，请稍后再试');
            }
          },
          (err) => {
            this.$Message.error(err.message);
          })
      },

      chooseTask(index) {
        this.indexOfTask = index;
        this.flag = 1;
        console.log(this.$refs.canvasForWorker)
        this.$refs.canvasForWorker.load_pic(this.show_data[index], 1);
      },

      // changeModel() {
      //   // this.tagModel = this.label;
      //   // setTimeout(this.changeModel,2000)
      //   if (this.tagModel === '区域标注') {
      //     this.$Message.success(this.tagModel);
      //     this.imgUrl = require('../../assets/background.jpg');
      //   }
      //   else if (this.tagModel === '方框标注') {
      //     this.$Message.success('方框标注');
      //     this.imgUrl = require('../../assets/background.jpg');
      //   } else {
      //     this.$Message.success('全局标注');
      //     this.imgUrl = require('../../assets/background.jpg');
      //   }
      // },


    }
  }
</script>

<style scoped>
  .imgStyle {
    width: 100%;
    height: 100%;
    /*height:200px;*/
  }
</style>
