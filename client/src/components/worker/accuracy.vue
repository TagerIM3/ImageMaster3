<template>
  <div>
      <Card style="min-height: 360px">
        <p slot="title">我的准确率</p>
        <div id="card" style="min-height: 280px;width:90%;left:5%"></div>
      </Card>
    </div>


</template>

<script>
  export default {
    name: "accuracy",
    mounted(){
      this.drawCard();
    },
    methods: {
      drawCard() {
        let myChart = this.$echarts.init(document.getElementById('card'));
        myChart.setOption({
          // title : {
          //   text: '我的准确率',
          // },
          tooltip : {
            // trigger: 'axis'
          },
          legend: {
            orient : 'vertical',
            x : 'right',
            y : 'bottom',
            data:['分布']
          },
          toolbox: {
            show : true,
            feature : {
              mark : {show: true},
              dataView : {show: true, readOnly: false},
              restore : {show: true},
              saveAsImage : {show: true}
            }
          },
          polar : [
            {
              indicator : [
                { text: '人物', max: 1},
                { text: '风景', max: 1},
                { text: '车辆', max: 1},
                { text: '动物', max: 1},
                { text: '其他', max: 1}
              ]
            }
          ],
          calculable : true,
          series : [
            {
              name: '任务类型',
              type: 'radar',
              data : [
                {
                  // value : [Math.floor(Math.random()*10),Math.floor(Math.random()*10),Math.floor(Math.random()*10),Math.floor(Math.random()*10),Math.floor(Math.random()*10)],
                  name : '分布',
                  value:[0.77,0.68,0.81,0.60,0.59]
                },
              ]
            }
          ]
        });
      }
    }
  }
</script>

<style scoped>

</style>
