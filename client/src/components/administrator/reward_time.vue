<template>
  <div style="min-height: 250px;width:85%;white-space: nowrap;padding-left: 15%">
    <Card style="height:500px">
      <p slot="title">影响任务用时的因素 </p>
      <RadioGroup v-model="dataMode" type="button" @on-change="showData">
        <Radio label="积分奖励"></Radio>
        <Radio label="图片数量"></Radio>
      </RadioGroup>

      <div id="card" style="height: 400px;width:100%;left:0%"></div>
    </Card>

    <!--<Card style="min-height: 550px">-->
    <!--<p slot="title">奖励与花费时间散点图</p>-->
    <!--<div id="card" style="height: 460px;width:800px;left:5%" ></div>-->
    <!--&lt;!&ndash;<div id="card7-1" style="height: 160px;width:90%;left:5%" v-if="showCard7 === 1">&ndash;&gt;-->
    <!--&lt;!&ndash;<Alert type="error" show-icon>加载失败，暂无数据</Alert>&ndash;&gt;-->
    <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</Card>-->
  </div>
</template>

<script>
  import adminAPI from '../../api/administrator'

  export default {
    data() {
      return {
        dataMode: '积分奖励',
        showCard7: 0,
        points: [],
        totalHour: [],
        imageNum: [],
        xData: [],
        yData: [],
        pointData: [],
      }
    },
    mounted() {
      this.showData();
      // this.drawCard7();
    },
    methods: {
      showData() {
        adminAPI.getStatistics((res) => {
            console.log(res);
            if (res.message === 'SUCCESS') {
              var adminSta = res.object;
              this.points = adminSta.points;
              this.totalHour = adminSta.totalHour;
              this.imageNum = adminSta.imageNum;
              this.pointData = [];

              //依次存入数据
              let arr = new Array();
              if(this.dataMode === '积分奖励'){

                for(let i=0;i<this.points.length;i++){
                  arr[i] = [];
                  arr[i][0] = this.points[i];
                  // this.pointData[i][0]=this.points[i];
                }
              }
              else{
                for(let i=0;i<this.imageNum.length;i++){
                  arr[i] = [];
                  arr[i][0] = this.imageNum[i];
                  // this.pointData[i][0]=this.imageNum[i];
                }
              }

              for(let i=0;i<this.totalHour.length;i++){
                arr[i][1] = this.totalHour[i];
                // this.pointData[i][1]=this.totalHour[i];
              }
              this.pointData = arr;
              console.log(this.pointData);
              if (this.dataMode === '积分奖励') {
                this.pointData = [
                  [1, 32.4], [1, 23.0], [1, 24.2], [1.3, 20.0], [1.5, 23.6],
                  [2.2, 19.0], [2.5, 17.6], [2.7, 19.8], [2.2, 16.8], [3.1, 25.2],
                  [3.0, 15.2], [3.9, 14.2], [3.9, 12.5], [3.4, 12.0], [3.0, 10.0],
                  [4.2, 7], [4.2, 19.2], [4.0,13.2], [4.0, 17.8], [4.6, 8.8],
                  [4.5, 10], [5.0, 9.2], [4.8, 6.3], [4.5, 8.7], [5.0, 6.5],
                ];
              } else {
                this.pointData = [
                  [168.3, 54.8], [180.3, 60.7], [165.5, 60.0], [165.0, 62.0], [164.5, 60.3],
                  [156.0, 52.7], [160.0, 74.3], [163.0, 62.0], [165.7, 73.1], [161.0, 80.0],
                  [162.0, 54.7], [166.0, 53.2], [174.0, 75.7], [172.7, 61.1], [167.6, 55.7],
                  [151.1, 48.7], [164.5, 52.3], [163.5, 50.0], [152.0, 59.3], [169.0, 62.5],
                ];
              }

              this.drawCard7();
              // this.drawCard();
            } else {
              // this.showCard7=1;
            }
          },
          (err) => {
            // this.showCard7=1;
            this.$Message.error(err.message);
          })
      },

      drawCard(){
        let myChart = this.$echarts.init(document.getElementById('card'));
        myChart.setOption(
          {
            title: {
              text: 'Google Trends',
              link: 'http://www.google.com/trends/hottrends'
            },
            tooltip: {
              show: true
            },
            series: [{
              name: 'Google Trends',
              type: 'wordCloud',
              size: ['80%', '80%'],
              textRotation : [0, 45, 90, -45],
              textPadding: 0,
              autoSize: {
                enable: true,
                minSize: 14
              },
              data: [
                {
                  name: "Sam S Club",
                  value: 10000,
                  itemStyle: {
                    normal: {
                      color: 'black'
                    }
                  }
                },
                {
                  name: "Macys",
                  value: 6181,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Amy Schumer",
                  value: 4386,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Jurassic World",
                  value: 4055,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Charter Communications",
                  value: 2467,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Chick Fil A",
                  value: 2244,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Planet Fitness",
                  value: 1898,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Pitch Perfect",
                  value: 1484,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Express",
                  value: 1112,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Home",
                  value: 965,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Johnny Depp",
                  value: 847,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Lena Dunham",
                  value: 582,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Lewis Hamilton",
                  value: 555,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "KXAN",
                  value: 550,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Mary Ellen Mark",
                  value: 462,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Farrah Abraham",
                  value: 366,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Rita Ora",
                  value: 360,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Serena Williams",
                  value: 282,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "NCAA baseball tournament",
                  value: 273,
                  // itemStyle: this.createRandomItemStyle()
                },
                {
                  name: "Point Break",
                  value: 265,
                  // itemStyle: this.createRandomItemStyle()
                }
              ]
            }]
          }
        );
      },
      createRandomItemStyle() {
        return {
          normal: {
            color: 'rgb(' + [
              Math.round(Math.random() * 160),
              Math.round(Math.random() * 160),
              Math.round(Math.random() * 160)
            ].join(',') + ')'
          }
        };
      },

      drawCard7() {
        let myChart = this.$echarts.init(document.getElementById('card'));

        myChart.setOption({
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
              data: this.pointData,
              // data: [
              //   [161.2, 51.6], [167.5, 59.0], [159.5, 49.2], [157.0, 63.0], [155.8, 53.6],
              //   [170.0, 59.0], [159.1, 47.6], [166.0, 69.8], [176.2, 66.8], [160.2, 75.2],
              //   [172.5, 55.2], [170.9, 54.2], [172.9, 62.5], [153.4, 42.0], [160.0, 50.0],
              //   [147.2, 49.8], [168.2, 49.2], [175.0, 73.2], [157.0, 47.8], [167.6, 68.8],
              //   [159.5, 50.6], [175.0, 82.5], [166.8, 57.2], [176.5, 87.8], [170.2, 72.8],
              //   [174.0, 54.5], [173.0, 59.8], [179.9, 67.3], [170.5, 67.8], [160.0, 47.0],
              //   [154.4, 46.2], [162.0, 55.0], [176.5, 83.0], [160.0, 54.4], [152.0, 45.8],
              //   [162.1, 53.6], [170.0, 73.2], [160.2, 52.1], [161.3, 67.9], [166.4, 56.6],
              //   [168.9, 62.3], [163.8, 58.5], [167.6, 54.5], [160.0, 50.2], [161.3, 60.3],
              //   [167.6, 58.3], [165.1, 56.2], [160.0, 50.2], [170.0, 72.9], [157.5, 59.8],
              //   [167.6, 61.0], [160.7, 69.1], [163.2, 55.9], [152.4, 46.5], [157.5, 54.3],
              //   [168.3, 54.8], [180.3, 60.7], [165.5, 60.0], [165.0, 62.0], [164.5, 60.3],
              //   [156.0, 52.7], [160.0, 74.3], [163.0, 62.0], [165.7, 73.1], [161.0, 80.0],
              //   [162.0, 54.7], [166.0, 53.2], [174.0, 75.7], [172.7, 61.1], [167.6, 55.7],
              //   [151.1, 48.7], [164.5, 52.3], [163.5, 50.0], [152.0, 59.3], [169.0, 62.5],
              //   [164.0, 55.7], [161.2, 54.8], [155.0, 45.9], [170.0, 70.6], [176.2, 67.2],
              //   [170.0, 69.4], [162.5, 58.2]
              // ]
            }]
        });
      }
    }
  }
</script>

<style scoped>

</style>
