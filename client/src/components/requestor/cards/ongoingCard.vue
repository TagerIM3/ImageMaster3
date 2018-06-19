<template>
  <div style="height: 250px;width:100%;white-space: nowrap">
    <Card>

      <div id="card4" style="height: 160px;width:100%;left:5%" v-if="showCard4===0">
        <Table :loading="loading" :columns="columns1" :data="show_data" @on-row-click="click_table" ellipsis></Table>
      </div>
      <div id="card2-1" style="height: 160px;width:100%;left:5%" v-if="showCard4===1">
        <Alert type="error" show-icon>加载失败，暂无数据</Alert>
      </div>
    </Card>
  </div>
</template>

<script>

  export default {
    data: function () {
      return {
        createUsers: [],
        showCard4: 0,
        loading: 1,
        columns1: [
          {
            title: 'ID',
            key: 'id'
          },
          {
            title: '任务状况',
            key: 'finish',
            render: (h, params) => {
              const row = params.row;
              let color = 0;
              if (row.finish) color = 100;
              return h('Progress', {
                props: {
                  percent: color,
                  status: 'active'
                },
              });
            }
          },
        ],
      }
    },
    mounted() {
      this.drawCard4();

    },
    methods: {
      showData() {
        this.drawCard4();
      },
      drawCard4() {
        let myChart4 = this.$echarts.init(document.getElementById('card4'));
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
            data: ['7', '6', '5', '4', '3', '2', '1']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '用户',
              type: 'line',
              stack: '总数',
              data: [

                this.createUsers[23],
                this.createUsers[24],
                this.createUsers[25],
                this.createUsers[26],
                this.createUsers[27],
                this.createUsers[28],
                this.createUsers[29]
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
