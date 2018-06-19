<template>
  <div style="height: 250px;width:100%;white-space: nowrap">
    <Card>
      <p slot="title">近30天完成任务数</p>
      <div id="card" style="height: 160px;width:90%;left:5%" v-if="showCard===0"></div>
      <div id="card_1" style="height: 160px;width:90%;left:5%" v-if="showCard===1">
        <Alert type="error" show-icon>加载失败，暂无数据</Alert>
      </div>
    </Card>
  </div>
</template>

<script>
  import workerAPI from '../../api/worker'

  export default {
    data() {
      return {
        finishTasks: [],
        showCard: 0
      }
    },
    mounted() {
      this.showData();
    },
    methods: {
      showData() {
        workerAPI.getStatistics((res) => {
            if (res.message === 'SUCCESS') {
              var workerSta = res.object;
              this.finishTasks = workerSta.finishTasks;
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
        let myChart = this.$echarts.init(document.getElementById('card'));
        myChart.setOption({
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
            data: ['7', '6', '5', '4', '3', '2', '1']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '完成任务数',
              type: 'line',
              stack: '总数',
              data: [0, 0, 0, 4, 0, 5, 0]
              // data: this.finishTasks
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
