<template>
  <div style="height: 250px;width:100%;white-space: nowrap">
    <Card>
      <p slot="title">近7天注册用户数</p>
      <div id="card4" style="height: 160px;width:90%;left:5%" v-if="showCard4===0"></div>
      <!--<div id="card2-1" style="height: 160px;width:90%;left:5%" v-if="showCard4===1">-->
        <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
  </div>
</template>

<script>
  import adminAPI from '../../api/administrator'

  export default {
    name: "createUsersInSeven",
    data() {
      return {
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
        adminAPI.getStatistics((res) => {
            if (res.message === 'SUCCESS') {
              var adminSta = res.object;
              this.createUsers = adminSta.createUsers;
              this.drawCard4();
              // this.$Message.success();
            } else {
              this.showCard4 = 1;
            }
          },
          (err) => {
            this.showCard4 = 1;
            this.$Message.error(err);
          })
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
      }
    }
  }
</script>

<style scoped>

</style>
