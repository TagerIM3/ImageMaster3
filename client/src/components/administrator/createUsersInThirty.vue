<template>
  <div style="height: 250px;width:100%;white-space: nowrap">
    <Card>
      <p slot="title">近30天注册用户数</p>
      <div id="card3" style="height: 160px;width:90%;left:5%" v-if="showCard3 === 0"></div>
      <!--<div id="card3-1" style="height: 160px;width:90%;left:5%" v-if="showCard3===1">-->
        <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
  </div>
</template>

<script>
  import adminAPI from '../../api/administrator'

  export default {
    name: "createUsersInThirty",
    data() {
      return {
        showCard3: 0,
        xArr: [],
        createUsers: [],
        yData: [],
        xData: [],
      }
    },
    mounted() {
      this.showData();
      // this.drawCard3();
    },
    methods: {
      showData() {
        adminAPI.getStatistics((res) => {
            if (res.message === 'SUCCESS') {
              var adminSta = res.object;
              this.createUsers = adminSta.createUsers;

              let xData30 = [];
              for (let i = 0; i < 30; i++) {
                let dd = new Date();
                dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
                let m = dd.getMonth() + 1;//获取当前月份的日期
                let d = dd.getDate();
                xData30[i] = m.toString() + '/' + d.toString();
              }
              this.xData = xData30;
              this.yData = this.createUsers;
              this.drawCard3();
            }
            // else {
            //   this.showCard3 = 1;
            // }

          },
          (err) => {
            this.$Message.error(err.message);
          })
      },
      drawCard3() {
        let myChart3 = this.$echarts.init(document.getElementById('card3'));
        myChart3.setOption({
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
            // data: ['25-30', '20-25', '15-20', '10-15', '5-10', '0-5']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '用户',
              type: 'line',
              stack: '总量',
              data: this.yData
              // data: [this.data1, this.data2, this.data3, this.data4, this.data5, this.data6]
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
