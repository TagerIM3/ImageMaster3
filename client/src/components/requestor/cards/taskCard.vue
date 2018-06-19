<template>
  <div style="width:100%;white-space: nowrap">

    <!--<Card style="min-height: 360px">-->
      <!--<p slot="title">近7天发布任务数</p>-->

      <!--&lt;!&ndash;<div id="card8-1" style="height: 160px;width:100%;" v-if="showCard8 === 1">&ndash;&gt;-->
      <!--&lt;!&ndash;<Alert type="error" show-icon>加载失败，暂无数据</Alert>&ndash;&gt;-->
      <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</Card>-->
    <div style="height: 5px"></div>
    <Card style="min-height: 360px">
      <p slot="title">近7天任务情况</p>


      <div id="card10" style="min-height: 200px;width:90%;left:5%" ></div>
      <!--<div id="card11" style="min-height: 200px;width:90%;left:5%" ></div>-->
      <!--<div id="card8-1" style="height: 160px;width:100%;" v-if="showCard8 === 1">-->
      <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
    <div style="height: 5px"></div>
    <Card style="min-height: 360px">
      <p slot="title">近30天任务情况</p>
      <div id="card8"  style="min-height: 200px;width:90%;left:5%" ></div>
      <!--<div id="card8-1" style="height: 160px;width:100%;" v-if="showCard8 === 1">-->
      <!--<Alert type="error" show-icon>加载失败，暂无数据</Alert>-->
      <!--</div>-->
    </Card>
    <div style="height: 5px"></div>
    <!--<Card style="min-height: 360px">-->
      <!--<p slot="title">近30天完成任务数</p>-->
      <!--<div id="card9" style="min-height: 200px;width:90%;left:5%" ></div>-->
      <!--&lt;!&ndash;<div id="card8-1" style="height: 160px;width:100%;" v-if="showCard8 === 1">&ndash;&gt;-->
      <!--&lt;!&ndash;<Alert type="error" show-icon>加载失败，暂无数据</Alert>&ndash;&gt;-->
      <!--&lt;!&ndash;</div>&ndash;&gt;-->
    <!--</Card>-->
  </div>
</template>

<script>
  export default {

    name: 'taskCard',
    data() {
      return {
        dataMode:"近七天数据",
        showCard8: 0,
        submitTasks: [],
        finishTasks: [],
        xArr: [],
        xArr_7:[],
        submitTasks_7: [],
        finishTasks_7: [],
      }
    },
    methods: {
      showData(submit,finish) {
        this.submitTasks = submit;
        this.finishTasks = finish;
        // for(var i=0;i<30;i++){
        //   this.xArr[i] = 30-i;
        // }
        for(let i=0;i<7;i++){
          this.submitTasks_7[i] = submit[23+i];
          this.finishTasks_7[i] = finish[23+i];
        }
        this.drawCard8();
        // this.drawCard9();
        this.drawCard10();
        // this.drawCard11();
      },
      drawCard11() {
        let myChart8 = this.$echarts.init(document.getElementById('card11'));
        let xData7 = [];
        for (let i = 0; i < 7; i++) {
          let dd = new Date();
          dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
          let m = dd.getMonth() + 1;//获取当前月份的日期
          let d = dd.getDate();
          xData7[i] = m.toString() + '/' + d.toString();
        }
        this.xArr_7 = xData7;
        console.log(this.xArr_7)
        myChart8.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['发布任务数']
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
            data: this.xArr_7
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '发布任务数',
              type: 'line',
              stack: '总量',
              data: this.submitTasks_7
            },
          ]
        });
      },
      drawCard10() {
        let myChart8 = this.$echarts.init(document.getElementById('card10'));
        let xData7 = [];
        for (let i = 0; i < 7; i++) {
          let dd = new Date();
          dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
          let m = dd.getMonth() + 1;//获取当前月份的日期
          let d = dd.getDate();
          xData7[i] = m.toString() + '/' + d.toString();
        }
        this.xArr_7 = xData7;
        console.log(this.xArr_7)
        myChart8.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['完成任务数','发布任务数']
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
            data: this.xArr_7
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '发布任务数',
              type: 'line',
              stack: '总量',
              data: this.submitTasks_7
            },
            {
              name: '完成任务数',
              type: 'line',
              // stack: '总量',
              data: this.finishTasks_7
            },
          ]
        });
      },
      drawCard8() {
        let myChart8 = this.$echarts.init(document.getElementById('card8'));
        let xData30 = [];
        for (let i = 0; i < 30; i++) {
          let dd = new Date();
          dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
          let m = dd.getMonth() + 1;//获取当前月份的日期
          let d = dd.getDate();
          xData30[i] = m.toString() + '/' + d.toString();
        }
        this.xArr = xData30;
        myChart8.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['完成任务数','发布任务数']
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
            data: this.xArr
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '完成任务数',
              type: 'line',
              // stack: '总量',
              data: this.finishTasks
            },
            {
              name: '发布任务数',
              type: 'line',
              // stack: '总量',
              data: this.submitTasks
            }

          ]
        });
      },

      drawCard9() {
        let myChart9 = this.$echarts.init(document.getElementById('card9'));

        let xData30 = [];
        for (let i = 0; i < 30; i++) {
          let dd = new Date();
          dd.setDate(dd.getDate() - (29 - i));//获取i天后的日期
          let m = dd.getMonth() + 1;//获取当前月份的日期
          let d = dd.getDate();
          xData30[i] = m.toString() + '/' + d.toString();
        }
        this.xArr = xData30;
        myChart9.setOption({
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: [ '完成任务数']
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
            data: this.xArr
          },
          yAxis: {
            type: 'value'
          },
          series: [

          ]
        });
      }

    }
  }
</script>

<style scoped>

</style>
