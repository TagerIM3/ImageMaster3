<template>
  <div>
    <Content :style="{padding: '0 50px'}">
      <Breadcrumb :style="{margin: '20px 0'}">
        <BreadcrumbItem></BreadcrumbItem>
      </Breadcrumb>

      <div style="min-height: 200px;">


        <task-card ref="taskCard"></task-card>
        <radar-card></radar-card>

        <!--<div>-->
          <!--<ongoingCard ref="ongoingCard"></ongoingCard>-->
        <!--</div>-->
        <!--<finish-task-in-week></finish-task-in-week>-->
      </div>

    </Content>
  </div>
</template>

<script>
  import api from '../../api/requestor'
  import taskCard from './cards/taskCard'
  import ongoingCard from './cards/ongoingCard'
  import finishTaskInWeek from '../worker/finishTasksInWeek'
  import radarCard from "./cards/radarCard"
    export default {
        name: "showInfo",
      components:{taskCard,ongoingCard,finishTaskInWeek,radarCard},
      mounted(){
        this.getStatistics()
      },
      methods: {
        getStatistics() {
          api.getStatistics(
            (res) => {
              // console.log(res)
              if (res.message === 'SUCCESS') {
                this.$refs.taskCard.showData(res.object.submitTasks,res.object.finishTasks)
                // this.$refs.ongoingCard.showData(res.object.taskNames,res.object.process)
              } else if (res.message === 'WRONG') {
                this.$Message.error('网络错误，请稍后再试')
              } else if (res.message === 'FAIL') {
                this.$Message.error('网络错误，请稍后再试')
              }
              else if(res.message === 'TIMEOUT'){
                this.$Message.error('您因为太久未操作而被登出！')
                this.$router.push({
                  path: '/',
                });
              }
            },
            (err) => {
              this.$Message.error(err.message);
            }
          )
        },
              }

    }
</script>

<style scoped>

</style>
