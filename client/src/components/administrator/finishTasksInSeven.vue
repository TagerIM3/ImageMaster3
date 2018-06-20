<template>
  <div style="height: 290px;width:85%;white-space: nowrap;padding-left: 15%">
    <Card>
      <p slot="title">近7天发布与完成任务数</p>
      <div id="card7" style="height: 200px;width:80%;left:5%" v-if="showCard7 === 0"></div>
      <div id="card7-1" style="height: 160px;width:80%;left:5%" v-if="showCard7 === 1">
        <Alert type="error" show-icon>加载失败，暂无数据</Alert>
      </div>
    </Card>

    <Modal
      v-model="viewTask"
      title="查看任务"
      @on-ok=""
      @on-cancel="">

      <Table :data="show_data" :columns="tableColumns2" stripe></Table>
      <div style="margin: 10px;overflow: hidden">
        <div style="float: right;">
          <Page :current="1" @on-change="changePage" page-size="5" :total="data_length"></Page>
        </div>
      </div>
    </Modal>

  </div>
</template>

<script>
  import adminAPI from '../../api/administrator'

  export default {
    name: "finishTasksInSeven",
    data() {
      return {
        indexOfPage: 1,
        viewTask:false,
        show_data: [],
        data: [],
        tableColumns2: [
          {
            'title': '任务名',
            'key': 'name',
          },
          {
            'title': '图片数量',
            'key': 'imageNum',
          },
          {
            'title': '积分奖励',
            'key': 'points',
          },
          {
            'title': '所需人数',
            'key': 'people',
          },

        ],
        showCard7: 0,
        submitTasks: [],
        finishTasks: [],
        xData: [],
        yData1: [],
        yData2: [],
      }
    },
    mounted() {
      this.showData();
      // this.drawCard7();
    },
    methods: {
      showData() {
        // adminAPI.getStatistics((res) => {
        //   console.log(res);
        //   if(res.message === 'SUCCESS'){
        //     var adminSta = res.object;
        //     this.submitTasks = adminSta.submitTasks;
        //     this.finishTasks = adminSta.finishTasks;
        //
        //     let xData7 = [];
        //     for (let i = 0; i < 7; i++) {
        //       let dd = new Date();
        //       dd.setDate(dd.getDate() - (6 - i));//获取i天后的日期
        //       let m = dd.getMonth() + 1;//获取当前月份的日期
        //       let d = dd.getDate();
        //       xData7[i] = m.toString() + '/' + d.toString();
        //       // this.yData[i] = this.createUsers[i + 23];
        //     }
        //     this.xData = xData7;
        //     console.log(xData7);
        //     this.drawCard7();
        //   }else{
        //     this.showCard7=1;
        //   }
        //   },
        //   (err) => {
        //     this.showCard7=1;
        //     this.$Message.error(err.message);
        //   })
        let xData7 = [];
        for (let i = 0; i < 7; i++) {
          let dd = new Date();
          dd.setDate(dd.getDate() - (6 - i));//获取i天后的日期
          let m = dd.getMonth() + 1;//获取当前月份的日期
          let d = dd.getDate();
          xData7[i] = m.toString() + '/' + d.toString();
          // this.yData[i] = this.createUsers[i + 23];
        }
        this.xData = xData7;
        this.submitTasks = [23, 35, 28, 29, 40, 37, 49];
        this.finishTasks = [18, 24, 37, 30, 33, 41, 42];
        this.drawCard7();

      },
      drawCard7() {
        let myChart7 = this.$echarts.init(document.getElementById('card7'));
        myChart7.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['发布任务数', '完成任务数']
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: this.xData
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '发布任务数',
              type: 'line',
              // stack: '总量',
              data: this.submitTasks,
              //   [
              //   this.submitTasks[23],
              //   this.submitTasks[24],
              //   this.submitTasks[25],
              //   this.submitTasks[26],
              //   this.submitTasks[27],
              //   this.submitTasks[28],
              //   this.submitTasks[29],
              // ]
            },
            {
              name: '完成任务数',
              type: 'line',
              // stack: '总量',
              data: this.finishTasks
              //   [
              //   this.finishTasks[23],
              //   this.finishTasks[24],
              //   this.finishTasks[25],
              //   this.finishTasks[26],
              //   this.finishTasks[27],
              //   this.finishTasks[28],
              //   this.finishTasks[29],
              // ]
            }
          ]
        });
        myChart7.on('click', params => {
          console.log(params.value);
          for (let i = 0; i < params.value-4; i++) {
            let data1 = {
              name: '人物标注_1',
              imageNum:5,
              points: 5,
              people: 10,
            };
            let data2 = {
              name: 'skin',
              imageNum:6,
              points: 3,
              people: 12,
            };
            let data3 = {
              name: 'hair_2',
              imageNum:8,
              points: 5,
              people: 20,
            };
            let data4 = {
              name: 'task',
              imageNum:15,
              points: 2,
              people: 10,
            };
            let data5 = {
              name: '外貌_2',
              imageNum:7,
              points: 3,
              people: 15,
            };
            this.data[i] = data1;
            this.data[i + 1] = data2;
            this.data[i + 2] = data3;
            this.data[i + 3] = data4;
            this.data[i + 4] = data5;
            i = i + 4;
          }
          this.show_data = this.data;
          this.data_length = this.show_data.length;
          this.viewTask = true;
          this.changePage('1');

        });

      },

      changePage(pageSize) {
        this.indexOfPage = pageSize;
        this.show_data=[];
        let loc = pageSize * 5;
        let i;
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

    }
  }
</script>

<style scoped>

</style>
