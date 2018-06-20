<template>
  <div style="height: 290px;width:85%;white-space: nowrap;padding-left: 15%">
    <Card>
      <p slot="title">近7天注册用户数</p>
      <div id="card4" style="height: 200px;width:80%;left:5%" v-if="showCard4===0"></div>
      <!--<div id="card2-1" style="height: 160px;width:90%;left:5%" v-if="showCard4===1">-->
      <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>

    <Modal
      v-model="viewUser"
      title="查看用户"
      @on-ok=""
      @on-cancel="">

      <Table :data="show_data" :columns="tableColumns1" stripe></Table>
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
    name: "createUsersInSeven",
    data() {
      return {
        indexOfPage: 1,
        viewUser: false,
        show_data: [],
        data: [],
        data_length:0,
        tableColumns1:[
          {
            'title': '用户名',
            'key': 'name',
          },
          {
            'title': '积分',
            'key': 'points',
          },
        ],
        createUsers: [],
        showCard4: 0,
        xData: [],
        yData: [],
      }
    },
    mounted() {
      this.showData();

    },
    methods: {
      showData() {
        // console.log('ppp')
        // adminAPI.getStatistics((res) => {
        //     if (res.message === 'SUCCESS') {
        //       var adminSta = res.object;
        //       this.createUsers = adminSta.createUsers;
        //       this.drawCard4();
        //       // this.$Message.success();
        //     } else {
        //       this.showCard4 = 1;
        //     }
        //   },
        //   (err) => {
        //     this.showCard4 = 1;
        //     this.$Message.error(err);
        //   })
        this.drawCard4();
      },
      drawCard4() {
        let myChart4 = this.$echarts.init(document.getElementById('card4'));

        let xData7 = [];
        for (let i = 0; i < 7; i++) {
          let dd = new Date();
          dd.setDate(dd.getDate() - (6 - i));//获取i天后的日期
          let m = dd.getMonth() + 1;//获取当前月份的日期
          let d = dd.getDate();
          xData7[i] = m.toString() + '/' + d.toString();
          this.yData[i] = this.createUsers[i + 23];
        }
        this.xData = xData7;
        this.yData = [21,12,9,18,15,12,30];
        myChart4.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['用户']
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
              name: '用户',
              type: 'line',
              stack: '总数',
              data: this.yData
            }
          ]
        });



        myChart4.on('click', params => {
          // this.$Message.success(params.series.data[5]);

          for (let i = 0; i < params.value-4; i++) {
            let data1 = {
              name: 'nju_lml',
              points: 105,
            };
            let data2 = {
              name: 'cc',
              points: 76,
            };
            let data3 = {
              name: 'Messi',
              points: 48,
            };
            let data4 = {
              name: '周cl',
              points: 92,
            };
            let data5 = {
              name: 'req',
              points: 93,
            };
            this.data[i] = data1;
            this.data[i + 1] = data2;
            this.data[i + 2] = data3;
            this.data[i + 3] = data4;
            this.data[i + 4] = data5;
            i = i + 4;
          }
          console.log(params.value);
          this.show_data = this.data;
          this.data_length = this.data.length;
          this.changePage('1');
          this.viewUser = true;

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
