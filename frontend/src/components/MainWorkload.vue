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
import Chart from "chart.js/auto";
import axios from "axios";
import { reactive } from "@vue/reactivity";

export default {

  data() {
    return {
      podRunCount: 0,
      podPendCount: 0,
      podSucCount: 0,
      podFailCount: 0,
      podUnknCount: 0,
      podLength: 0,
      deplReplicas: 0,
      deplReadyRep: 0,
    }
  },

  methods: {
    getPodPreview() {
      const podState = reactive({
        items: []
      })

      axios.get(this.$store.getters.uri + 'pod/listall').then(({ data }) => {
        podState.items = data;

        console.log("pod");
        console.log(podState.items);

        for (let i = 0; i < podState.items.length; i++) {
          if (podState.items[i].status == "Running") {
            this.podRunCount++;
          }
        }
        for (let i = 0; i < podState.items.length; i++) {
          if (podState.items[i].status == "Pending") {
            this.podPendCount++;
            console.log(podState.items[i].status);
          }
        }
        for (let i = 0; i < podState.items.length; i++) {
          if (podState.items[i].status == "Succeeded") {
            this.podSucCount++;
            console.log(podState.items[i].status);
          }
        }
        for (let i = 0; i < podState.items.length; i++) {
          if (podState.items[i].status == "Failed") {
            this.podFailCount++;
            console.log(podState.items[i].status);
          }
        }
        for (let i = 0; i < podState.items.length; i++) {
          if (podState.items[i].status == "Unknown") {
            this.podUnknCount++;
            console.log(podState.items[i].status);
          }
        }

        this.podLength = podState.items.length;

        console.log("podRunCount = " + this.podRunCount);
        console.log("podPendCount = " + this.podPendCount);
        console.log("podSucCount = " + this.podSucCount);
        console.log("podFailCount = " + this.podFailCount);
        console.log("podUnknCount = " + this.podUnknCount);
        console.log("podLength = " + this.podLength);
        this.drawPodChart();
      });

    },

    getDeplPreview() {
      const deplState = reactive({
        things: []
      })
      axios.get(this.$store.getters.uri + 'deployment/listall').then(({ data }) => {
        deplState.things = data;
        console.log("deployment");
        console.log(deplState.things.items);

        for (var i = 0; i < deplState.things.items.length; i++) {
          if (deplState.things.items[i].status.replicas != 0) {
            this.deplReplicas = this.deplReplicas + deplState.things.items[i].status.replicas;
          }
        }

        for (var j = 0; j < deplState.things.items.length; j++) {
          if (deplState.things.items[j].status.readyReplicas != undefined && deplState.things.items[j].status.readyReplicas != 0) {
            this.deplReadyRep = this.deplReadyRep + deplState.things.items[j].status.readyReplicas;
          }

        }
        console.log("deplReplicas = " + this.deplReplicas);
        console.log("deplReadyRep = " + this.deplReadyRep);
        this.drawDeplChart();
      })
    }


    ,
    drawPodChart() {
      const ctx = document.getElementById("podChart");

      const podChart = new Chart(ctx, {
        type: "pie",
        data: {
          labels: ["Running", "Pending", "Succeeded", "Failed", "Unknown"],

          datasets: [
            {
              label: "pod",
              data: [this.podRunCount, this.podPendCount, this.podSucCount, this.podFailCount, this.podUnknCount],
              backgroundColor: [
                "rgb(54, 162, 235)",
                "rgb(153, 51, 255)",
                "rgb(0, 204, 0)",
                "rgb(255, 99, 132)",
                "rgb(255, 204, 0)",
              ],
              hoverOffset: 4,
            },
          ],
        },
        options: {
          plugins: {
            legend: {
              position: "left",
            }
          }
        }
      });

      podChart;
    },

    drawDeplChart() {
      const ctx = document.getElementById("deplChart");

      const deplChart = new Chart(ctx, {
        type: "pie",
        data: {
          labels: ["Ready", "Not Ready"],
          datasets: [
            {
              label: "deployment",
              data: [this.deplReadyRep, (this.deplReplicas - this.deplReadyRep)],
              backgroundColor: [
                "rgb(54, 162, 235)",
                "rgb(255, 99, 132)",
              ],
              hoverOffset: 4,
            },
          ],
        },
        options: {
          plugins: {
            legend: {
              position: "left",
            }
          }
        }
      });

      deplChart;
    },
  },
  mounted() {
    this.getPodPreview();
    this.getDeplPreview();

  },
};

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