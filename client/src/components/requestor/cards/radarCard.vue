<template>
  <div>
    <Card>
      <p slot="title">任务类型</p>
      <div id="card1" style="min-height: 300px;width:90%;left:5%"></div>
    </Card>
  </div>
</template>

<script>
    export default {
        name: "radarCard",
      mounted(){
        this.show_card()
      },
      methods:{
        show_card(){
          let myChart = this.$echarts.init(document.getElementById("card1"));
          myChart.setOption({
            title : {
              text: '任务类型',
            },
            tooltip : {
              trigger: 'axis'
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
                  { text: '人物', max: 10},
                  { text: '风景', max: 10},
                  { text: '车辆', max: 10},
                  { text: '动物', max: 10},
                  { text: '其他', max: 10}
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
                    value : [Math.floor(Math.random()*10),Math.floor(Math.random()*10),Math.floor(Math.random()*10),Math.floor(Math.random()*10),Math.floor(Math.random()*10)],
                    name : '分布'
                  },
                ]
              }
            ]
          })
        },
      },
    }
</script>

<style scoped>

</style>
