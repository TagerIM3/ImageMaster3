<template>
  <div>
  <div style="width:100%;white-space: nowrap;">
    <Card style="min-height: 360px">
      <p slot="title">近7天完成任务数</p>
      <!--<RadioGroup v-model="dataMode" type="button" @on-change="drawCard">-->
        <!--<Radio label="近7天数据"></Radio>-->
        <!--<Radio label="近30天数据"></Radio>-->
      <!--</RadioGroup>-->

      <div id="card1" style="min-height: 200px;width:90%;left:5%"></div>
      <!--<div id="card_1" style="height: 160px;width:90%;left:5%" v-if="showCard===1">-->
        <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
  </div>

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
  <!--<div style="width:100%;padding-top: 3%">-->
    <!--<Card style="min-height: 360px">-->
      <!--<p slot="title">近30天完成任务数</p>-->

      <!--<div id="card2" style="min-height: 200px;width:90%;left:5%"></div>-->
      <!--&lt;!&ndash;<div id="card_1" style="height: 160px;width:90%;left:5%" v-if="showCard===1">&ndash;&gt;-->
      <!--&lt;!&ndash;<Alert type="error" show-icon>加载失败，暂无数据</Alert>&ndash;&gt;-->
      <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</Card>-->
  <!--</div>-->
  </div>
</template>

<script>
  import workerAPI from '../../api/worker'

  export default {
    name: "finishTasksInWeek",
    data() {
      return {
        indexOfPage: 1,
        data: [],
        data_length:0,
        viewTask: false,
        finishTasks: [],
        showCard: 0,
        dataMode: '近7天数据',
        xData1: [],
        xData2: [],
        yData: [],
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
      }
    },
    mounted() {
      this.showData();
    },
    methods: {
      showData() {
        workerAPI.getStatistics((res) => {
          console.log(res);
            if (res.message === 'SUCCESS') {
              var workerSta = res.object;
              this.finishTasks = workerSta.submitTasks;
              this.drawCard();
            }
            else if (res.message === 'TIMEOUT') {
              this.$Message.error('您因为太久未操作而被登出！');
              this.$router.push({
                path: '/',
              });
            }
            else {
              this.showCard = 1;
            }
          },
          (err) => {
            this.showCard = 1;
          }
        )
      },
      drawCard() {
        let myChart1 = this.$echarts.init(document.getElementById('card1'));
        // let myChart2 = this.$echarts.init(document.getElementById('card2'));

          let xData7 = [];
          for (let i = 0; i < 7; i++) {
            let dd = new Date();
            dd.setDate(dd.getDate() - (6 - i));//获取i天后的日期
            let m = dd.getMonth() + 1;//获取当前月份的日期
            let d = dd.getDate();
            xData7[i] = m.toString() + '/' + d.toString();
            this.yData[i] = this.finishTasks[i + 23];
          }
          this.xData1 = xData7;
          // console.log(this.xData);

          let xData30 = [];
          for (let i = 0; i < 30; i++) {
            let dd = new Date();
            dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
            let m = dd.getMonth() + 1;//获取当前月份的日期
            let d = dd.getDate();
            xData30[i] = m.toString() + '/' + d.toString();
          }
          this.xData2 = xData30;
          this.yData = this.finishTasks;
          // console.log(this.xData);
        this.yData = [6,8,7,12,10,7,9];


        // console.log(y);
        // console.log(m);
        // console.log(d);

        myChart1.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['完成任务数']
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
            data: this.xData1
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '完成任务数',
              type: 'line',
              stack: '总数',
              // data: [
              //   this.yData[23],
              //   this.yData[24],
              //   this.yData[25],
              //   this.yData[26],
              //   this.yData[27],
              //   this.yData[28],
              //   this.yData[29],
              // ]
              data: this.yData
            }
          ]
        });

        myChart1.on('click', params => {
          console.log(params.value);
          for (let i = 0; i < params.value-4; i++) {
            let data1 = {
              name: '风景标注2',
              imageNum:5,
              points: 5,
              people: 10,
            };
            let data2 = {
              name: 'animals',
              imageNum:6,
              points: 3,
              people: 12,
            };
            let data3 = {
              name: 'AllCars',
              imageNum:8,
              points: 5,
              people: 16,
            };
            let data4 = {
              name: 'cats',
              imageNum:7,
              points: 2,
              people: 10,
            };
            let data5 = {
              name: 'classmates',
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

        // myChart2.setOption({
        //   tooltip: {
        //     trigger: 'axis'
        //   },
        //   legend: {
        //     data: ['完成任务数']
        //   },
        //   grid: {
        //     left: '3%',
        //     right: '4%',
        //     bottom: '3%',
        //     containLabel: true
        //   },
        //   toolbox: {
        //     feature: {
        //       saveAsImage: {}
        //     }
        //   },
        //   xAxis: {
        //     type: 'category',
        //     boundaryGap: false,
        //     data: this.xData2
        //   },
        //   yAxis: {
        //     type: 'value'
        //   },
        //   series: [
        //     {
        //       name: '完成任务数',
        //       type: 'line',
        //       stack: '总数',
        //       data: this.yData
        //       // data: this.finishTasks
        //     }
        //   ]
        // });

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
