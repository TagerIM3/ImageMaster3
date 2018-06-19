<template>
  <div style="position:relative;width:100%;">
    <Card>
      <p slot="title">用户排名榜</p>

      <div v-if="showCard===0">

        <Table :data="tableData1" :columns="tableColumns1" stripe></Table>
        <div style="margin: 10px;overflow: hidden">
          <div style="float: right;">
            <Page :total="20" :current="1" @on-change="changePage"></Page>
          </div>
        </div>

      </div>

      <div id="card_1" style="height: 160px;width:90%;left:5%" v-if="showCard===1">
        <Alert type="error" show-icon>加载失败，暂无数据</Alert>
      </div>

    </Card>
  </div>
</template>

<script>
  import workerAPI from '../../api/worker'

  export default {
    name: "topRanking",
    data() {
      return {
        usernames: [],
        grades: [],
        showCard: 0,
        tableData1: [],
        // tableData1: this.mockTableData1(),
        tableColumns1: [
          {
            title: '用户名',
            key: 'name'
          },
          {
            title: '分数',
            key: 'status',
            // render: (h, params) => {
            //   const row = params.row;
            //   const color = row.status === 1 ? 'blue' : row.status === 2 ? 'green' : 'red';
            //   const text = row.status === 1 ? 'Working' : row.status === 2 ? 'Success' : 'Fail';
            //
            //   return h('Tag', {
            //     props: {
            //       type: 'dot',
            //       color: color
            //     }
            //   }, text);
            // }
          },
          // {
          //   title: 'Portrayal',
          //   key: 'portrayal',
          //   // render: (h, params) => {
          //   //   return h('Poptip', {
          //   //     props: {
          //   //       trigger: 'hover',
          //   //       title: params.row.portrayal.length + 'portrayals',
          //   //       placement: 'bottom'
          //   //     }
          //   //   }, [
          //   //     h('Tag', params.row.portrayal.length),
          //   //     h('div', {
          //   //       slot: 'content'
          //   //     }, [
          //   //       h('ul', this.tableData1[params.index].portrayal.map(item => {
          //   //         return h('li', {
          //   //           style: {
          //   //             textAlign: 'center',
          //   //             padding: '4px'
          //   //           }
          //   //         }, item)
          //   //       }))
          //   //     ])
          //   //   ]);
          //   // }
          //
          // },
          // {
          //   title: 'People',
          //   key: 'people',
          //   // render: (h, params) => {
          //   //   return h('Poptip', {
          //   //     props: {
          //   //       trigger: 'hover',
          //   //       title: params.row.people.length + 'customers',
          //   //       placement: 'bottom'
          //   //     }
          //   //   }, [
          //   //     h('Tag', params.row.people.length),
          //   //     h('div', {
          //   //       slot: 'content'
          //   //     }, [
          //   //       h('ul', this.tableData1[params.index].people.map(item => {
          //   //         return h('li', {
          //   //           style: {
          //   //             textAlign: 'center',
          //   //             padding: '4px'
          //   //           }
          //   //         }, item.n + '：' + item.c + 'People')
          //   //       }))
          //   //     ])
          //   //   ]);
          //   // }
          // },
          // {
          //   title: 'Sampling Time',
          //   key: 'time',
          //   render: (h, params) => {
          //     return h('div', 'Almost' + params.row.time + 'days');
          //   }
          // },
          // {
          //   title: 'Updated Time',
          //   key: 'update',
          //   render: (h, params) => {
          //     return h('div', this.formatDate(this.tableData1[params.index].update));
          //   }
          // }
        ]
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
              this.usernames = workerSta.usernames;
              this.grades = workerSta.grades;
              this.tableData1.push({
                'name': this.usernames,
                'status': this.grades
              });

            } else if (res.message === 'TIMEOUT') {
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

      // mockTableData1() {
      //   let data = [];
      //
      //   // for (let i = 0; i < 10; i++) {
      //   //   data.push({
      //   //     name: 'Business' + Math.floor(Math.random() * 100 + 1),
      //   //     status: Math.floor(Math.random() * 3 + 1),
      //   //     portrayal: ['City', 'People', 'Cost', 'Life', 'Entertainment'],
      //   //     people: [
      //   //       {
      //   //         n: 'People' + Math.floor(Math.random() * 100 + 1),
      //   //         c: Math.floor(Math.random() * 1000000 + 100000)
      //   //       },
      //   //       {
      //   //         n: 'People' + Math.floor(Math.random() * 100 + 1),
      //   //         c: Math.floor(Math.random() * 1000000 + 100000)
      //   //       },
      //   //       {
      //   //         n: 'People' + Math.floor(Math.random() * 100 + 1),
      //   //         c: Math.floor(Math.random() * 1000000 + 100000)
      //   //       }
      //   //     ],
      //   //     time: Math.floor(Math.random() * 7 + 1),
      //   //     update: new Date()
      //   //   })
      //   // }
      //   return data;
      // },

    }
  }
</script>

<style scoped>

</style>
