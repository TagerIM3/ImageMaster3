<template>
  <div style="min-height: 250px;width:100%;white-space: nowrap">

    <Card style="height:500px">
      <p slot="title">奖励与花费时间散点图</p>
      <div id="card2" style="height: 400px;width:100%;left:5%" ></div>
    </Card>
  </div>
</template>

<script>
  import adminAPI from '../../api/administrator'
  export default {
    data() {
      return {
        showCard7: 0,
        imageNum: [],
        totalTime: [],
        xData: [],
        yData1: [],
        yData2: [],
      }
    },
    mounted() {
      // this.showData();
      this.drawCard();
    },
    methods: {
      showData() {
        adminAPI.getStatistics((res) => {
            console.log(res);
            if(res.message === 'SUCCESS'){
              var adminSta = res.object;
              this.imageNum = adminSta.imageNum;
              this.totalTime = adminSta.totalHour;

              this.drawCard();
            }else{
              // this.showCard7=1;
            }
          },
          (err) => {
            // this.showCard7=1;
            this.$Message.error(err.message);
          })
      },
      drawCard() {
        let myChart_2 = this.$echarts.init(document.getElementById('card2'));

        myChart_2.setOption({
          xAxis: {
            scale: true
          },
          yAxis: {
            scale: true
          },
          series: [
            //   {
            //   type: 'effectScatter',
            //   symbolSize: 20,
            //   data: [
            //     [172.7, 105.2],
            //     [153.4, 42]
            //   ]
            // },
            {
              type: 'scatter',
              data: [
                [161.2, 51.6], [167.5, 59.0], [159.5, 49.2], [157.0, 63.0], [155.8, 53.6],
                [170.0, 59.0], [159.1, 47.6], [166.0, 69.8], [176.2, 66.8], [160.2, 75.2],
                [172.5, 55.2], [170.9, 54.2], [172.9, 62.5], [153.4, 42.0], [160.0, 50.0],
                [147.2, 49.8], [168.2, 49.2], [175.0, 73.2], [157.0, 47.8], [167.6, 68.8],
                [159.5, 50.6], [175.0, 82.5], [166.8, 57.2], [176.5, 87.8], [170.2, 72.8],
                [174.0, 54.5], [173.0, 59.8], [179.9, 67.3], [170.5, 67.8], [160.0, 47.0],
                [154.4, 46.2], [162.0, 55.0], [176.5, 83.0], [160.0, 54.4], [152.0, 45.8],
                [162.1, 53.6], [170.0, 73.2], [160.2, 52.1], [161.3, 67.9], [166.4, 56.6],
                [168.9, 62.3], [163.8, 58.5], [167.6, 54.5], [160.0, 50.2], [161.3, 60.3],
                [167.6, 58.3], [165.1, 56.2], [160.0, 50.2], [170.0, 72.9], [157.5, 59.8],
                [167.6, 61.0], [160.7, 69.1], [163.2, 55.9], [152.4, 46.5], [157.5, 54.3],
                [168.3, 54.8], [180.3, 60.7], [165.5, 60.0], [165.0, 62.0], [164.5, 60.3],
                [156.0, 52.7], [160.0, 74.3], [163.0, 62.0], [165.7, 73.1], [161.0, 80.0],
                [162.0, 54.7], [166.0, 53.2], [174.0, 75.7], [172.7, 61.1], [167.6, 55.7],
                [151.1, 48.7], [164.5, 52.3], [163.5, 50.0], [152.0, 59.3], [169.0, 62.5],
              ]
            }]
        });
      }
    }
  }
</script>

<style scoped>

</style>
