<template>
  <div>
    <Card>
      <p slot="title">任务类型</p>
      <div id="card1" style="min-height: 300px;width:90%;left:5%"></div>

      <Modal
        v-model="viewTask"
        title="查看任务"
        @on-ok=""
        @on-cancel="">

        <Table :data="show_data" :columns="tableColumns2" stripe></Table>
        <div style="margin: 10px;overflow: hidden">
          <div style="float: right;">
            <Page :current="1" page-size="5" ></Page>
          </div>
        </div>
      </Modal>
    </Card>
  </div>
</template>

<script>
    export default {
        name: "radarCard",
      data(){
          return{
            viewTask:false,
            show_data:[{
              name: '人物_1',
              imageNum:Math.floor(Math.random()*10),
              points: Math.floor(Math.random()*10),
              people: Math.floor(Math.random()*10),
            },
              {
                name: '人物_2',
                imageNum:Math.floor(Math.random()*10),
                points: Math.floor(Math.random()*10),
                people: Math.floor(Math.random()*10),
              },
              {
                name: '人物_3',
                imageNum:Math.floor(Math.random()*10),
                points: Math.floor(Math.random()*10),
                people: Math.floor(Math.random()*10),
              },
              {
                name: '人物_4',
                imageNum:Math.floor(Math.random()*10),
                points: Math.floor(Math.random()*10),
                people: Math.floor(Math.random()*10),
              },
              {
                name: '人物_5',
                imageNum:Math.floor(Math.random()*10),
                points: Math.floor(Math.random()*10),
                people: Math.floor(Math.random()*10),
              }
            ],
            tableColumns2: [
              {
                'title': '任务名',
                'key': 'name',
              },
              {
                'title': '图片数量',
                'key': 'imageNum',
              },
              {
                'title': '积分奖励',
                'key': 'points',
              },
              {
                'title': '所需人数',
                'key': 'people',
              },

            ],
          }
      },
      mounted(){

        this.show_card()
      },
      methods:{
        show_card(){
          let myChart = this.$echarts.init(document.getElementById("card1"));
          myChart.setOption( {
            tooltip: {
              trigger: 'item',
              formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            legend: {
              orient: 'vertical',
              x: 'left',
              data:['人物','动物','风景','车辆','其他']
            },
            series: [
              {
                name:'任务类型',
                type:'pie',
                radius: ['50%', '70%'],
                avoidLabelOverlap: false,
                label: {
                  normal: {
                    show: false,
                    position: 'center'
                  },
                  emphasis: {
                    show: true,
                    textStyle: {
                      fontSize: '30',
                      fontWeight: 'bold'
                    }
                  }
                },
                labelLine: {
                  normal: {
                    show: false
                  }
                },
                data:[
                  {value:Math.floor(Math.random()*50), name:'人物'},
                  {value:Math.floor(Math.random()*50), name:'动物'},
                  {value:Math.floor(Math.random()*50), name:'风景'},
                  {value:Math.floor(Math.random()*50), name:'车辆'},
                  {value:Math.floor(Math.random()*50), name:'其他'}
                ]
              }
            ]
          })
          myChart.on('click', params => {
            // this.$Message.success(params.data.name);
            console.log(this.show_data);
            this.viewTask = true;

          });
        },
      },
    }
</script>

<style scoped>

</style>
