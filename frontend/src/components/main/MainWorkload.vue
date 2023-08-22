<template>
  <div>
    <ul>
      <th>워크로드 상태</th>
    </ul>
    <br/>
    <ul>
      <div class="row row-cols-1 row-cols-md-2 g-4">
        <div class="col">
          <div class="card h-100">
            <canvas class="chart" id="deplChart"></canvas>
            <div class="card-body">
              <p class="card-text">
                {{ deplReadyRep }} / {{ deplReplicas }} 실행 중
                <span class="card-more">
              <router-link class="more" to="/deployment">...more</router-link> 
            </span>
              </p>
            </div>
            <div class="card-footer">
              <router-link class="card-title" to="/deployment">
                디플로이먼트
              </router-link>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card h-100">
            <canvas class="chart" id="podChart"></canvas>
            <div class="card-body">
              <p class="card-text">
                {{ podRunCount }} / {{ podLength }} 실행 중
                <span class="card-more">
               <router-link class="more" to="/pod">...more</router-link> 
               </span>
              </p>
            </div>
            <div class="card-footer">
              <router-link class="card-title" to="/pod">
                파드
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </ul>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios' // Assuming you have axios installed
import Chart from 'chart.js/auto'

export default {
    setup() {
        const podRunCount = ref(0)
        const podPendCount = ref(0)
        const podSucCount = ref(0)
        const podFailCount = ref(0)
        const podUnknCount = ref(0)
        const podLength = ref(0)
        const deplReplicas = ref(0)
        const deplReadyRep = ref(0)

        const podState = reactive({ items: [] })
        const deplState = reactive( { items: [] })

        const getPodPreview = async () => {
            const { data } = await axios.get(`pod/listall`);
            podState.items = data.list
            podLength.value = data.count
            for(let pod of podState.items){
                const status = pod.phase
                switch (status) {
                    case "Running":
                        podRunCount.value++
                        break
                    case "Pending":
                        podPendCount.value++
                        break
                    case "Succeeded":
                        podSucCount.value++
                        break
                    case "Failed":
                        podFailCount.value++
                        break
                    case "Unknown":
                        podUnknCount.value++
                        break
                }
            }
            drawPodChart()
        }

        const getDeplPreview = async () => {
            const { data } = await axios.get(`deployment/listall`);
                deplState.items = data.list

                for (const item of deplState.items) {
                        deplReplicas.value += item.replicaCount
                        deplReadyRep.value += item.readyReplicas
                }
                drawDeplChart()
        }

        function drawPodChart() {
            const ctx = document.getElementById("podChart")

            new Chart(ctx, {
                type: "pie",
                data: {
                    labels: ["Running", "Pending", "Succeeded", "Failed", "Unknown"],
                    datasets: [
                        {
                            label: "pod",
                            data: [podRunCount.value, podPendCount.value, podSucCount.value, podFailCount.value, podUnknCount.value],
                            backgroundColor: ["rgb(54, 162, 235)", "rgb(153, 51, 255)", "rgb(0, 204, 0)", "rgb(255, 99, 132)", "rgb(255, 204, 0)"],
                            hoverOffset: 4,
                        }
                    ]
                },
                options: {
                    plugins: {
                        legend: { position: "left" }
                    }
                }
            })
        }

        function drawDeplChart() {
            const ctx = document.getElementById("deplChart")

            new Chart(ctx, {
                type: "pie",
                data: {
                    labels: ["Ready", "Not Ready"],
                    datasets: [
                        {
                            label: "deployment",
                            data: [deplReadyRep.value, deplReplicas.value - deplReadyRep.value],
                            backgroundColor: ["rgb(54, 162, 235)", "rgb(255, 99, 132)"],
                            hoverOffset: 4,
                        }
                    ]
                },
                options: {
                    plugins: {
                        legend: { position: "left" }
                    }
                }
            })
        }

        onMounted(() => {
            getPodPreview()
            getDeplPreview()
        })

        return {
            podRunCount,
            podPendCount,
            podSucCount,
            podFailCount,
            podUnknCount,
            podLength,
            deplReplicas,
            deplReadyRep,
        }
    }
}
</script>

<style>
.col {
  margin: 0 auto;
  width: 450px;
  height: 470px;
}

.chart {
  margin: 0 auto;
  width: 350px !important;
  height: 350px !important;
}

.card-title {
  margin: 0 auto;
  font-size: 120%;
  color: black;
  text-decoration: none;
}

.card-more {
  justify-content: right;
  align-items: center;
  display: flex;
  margin-top: -20px;
}

.more {
  color: black;
}
</style>