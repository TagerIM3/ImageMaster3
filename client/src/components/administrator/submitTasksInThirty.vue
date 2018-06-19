<template>
  <div style="height: 250px;width:70%;white-space: nowrap">
    <Card>
      <p slot="title">近30天发布任务数</p>
      <div id="card6" style="height: 160px;width:90%;left:5%"></div>
    </Card>
  </div>
</template>

<script>
  import userAPI from '../../api/administrator'

  export default {
    name: "submitTasksInThirty",
    mounted() {
      this.drawCard6();
    },
    methods: {
      drawCard6() {
        let myChart6 = this.$echarts.init(document.getElementById('card6'));
        myChart6.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['工人', '发起者']
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
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '工人',
              type: 'line',
              stack: '总量',
              data: [120, 97, 78, 134, 90, 230, 210]
            },
            {
              name: '发起者',
              type: 'line',
              stack: '总量',
              data: [125, 182, 191, 234, 290, 167, 310]
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
