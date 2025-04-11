<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/YEASEUL-JANG/KubeBoard">
    <img src="frontend/src/assets/images/kubeLogo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Kube Board</h3>

  <p align="center">
    ⚙️ k8s client APi를 통해 k8s 자원을 관리할 수 있는 웹페이지
   </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#function">Function</a></li>
    <li><a href="#pages">Pages</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>
<p></p>


<!-- ABOUT THE PROJECT -->
## About The Project
<p>
💡 ‘KubeBoard’ 는 로컬 쿠버네티스 클러스터 환경을 UI상에서 쉽게 조회할 수 있는 K8S Dashboard 입니다.</p><p>
💡 각 서비스를 Microservices Architecture(MSA) 프로젝트로 구성하여 독립적인 단위로 개발/관리가 가능하도록 했습니다.</p><p>
💡 Kubernetes 클러스터 환경에서의 Pod, Deployment, Service 에 대한 생성/삭제/조회가 가능합니다.
</p>

<img src="frontend/src/assets/images/mainPage.png" alt="MainPage"
  width="700" height="500">
<br>
<p>
💡 ‘Pod’, 'Deployment', 'Service', 'User', 'Log' 등 다양한 도메인의 마이크로 서비스로 구성되어 각각의 독립적인 배포 및 확장이 가능합니다.
</p>

<img src="frontend/src/assets/images/dockerimage.png" alt="DockerPage"
  width="700" height="500">
<br>
<p>
💡 조회 데이터는 30초 단위로 실시간 업데이트되며, 실제 터미널 조회DATA와 동일합니다. 
</p>
<p>
<figure>
    <img src="frontend/src/assets/images/listPage.png" alt="ListPage"
  width="700" height="300" >
  <figcaption>[kubeboard] - Pod목록 조회</figcaption>
</figure>
</p><p>
  <figure>
   <img src="frontend/src/assets/images/terminal.png" alt="Terminal"
  width="700" height="150">  
  <figcaption>[terminal] - Pod목록 조회</figcaption>
</figure>
</p>
<p align="right">(<a href="#readme-top">back to top</a>)</p>



## Built With

| Category        | Stack                                                                 |
|-----------------|-----------------------------------------------------------------------|
| **Languages**   | Kotlin, Java, JavaScript, HTML/CSS                                    |
| **Frontend**    | Vue3, Bootstrap                                                       |
| **Backend**     | Spring Boot, Spring Cloud, Kafka, JPA (Querydsl), Quartz Batch        |
| **Tool/DevOps** | Minikube, Docker, IntelliJ, MySQL Workbench, GitHub, Circuit Breaker  |


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

0. Minikube Start
   ```sh
   minikube start
   ```
1. Clone the repo
   ```sh
   git clone https://github.com/YEASEUL-JANG/KubeBoard.git
   ```
2. Move to Frontend Folder
   ```sh
   cd frontend
   ```   
3. Install NPM packages
   ```sh
   npm install
   ```
4. Run a application and vue3 page 
   ```sh
   npm run serve
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- Function -->
## Function

- [x] k8s fabric client API를 활용한 k8s 데이터 조회/생성/삭제/스케일링 기능
- [x] 사용자 활동기록을 위한 실시간 log 데이터 축적
- [x] Quartz Batch 로 30초 간격 k8s 데이터 동기화 
- [x] DB 구조 설계 (JPA)
- [x] QuaryDSL을 활용한 다중 검색기능
- [x] SpringConfig 를 통한 서비스 구성정보 중앙 별도 관리
- [x] Kafka를 통한 메시지 기반 비동기 통신
- [x] Circuit Breaker를 활용한 장애발생 예방
- [x] Docker Image 빌드를 통한 각 마이크로서비스 도커 배포 

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- Pages -->
## Pages


### MainPage
<figure>
    <img src="frontend/src/assets/images/mainPage.png" alt="MainPage"
  width="700" height="500" >
  <figcaption>파드와 디플로이먼트의 현재 실행상태를 차트화, 서비스 데이터를 테이블로 나타냄</figcaption>
</figure>


### ListPage
<img src="frontend/src/assets/images/podList.png" alt="podList"
  width="700" height="300" >
  <img src="frontend/src/assets/images/deplList.png" alt="deplList"
  width="700" height="300" >
  <img src="frontend/src/assets/images/serviceList.png" alt="serviceList"
  width="700" height="300" >


### DetailListPage
<figure>
    <img src="frontend/src/assets/images/detailPage.png" alt="DetailPage"
  width="700" height="500" >
  <figcaption>상세페이지 예시</figcaption>
</figure>


### MYPAGE
<figure>
    <img src="frontend/src/assets/images/logPage.png" alt="LogPage"
  width="700" height="500" >
  <figcaption>개인 정보 및 활동기록</figcaption>
</figure>


### CreateModal
<figure>
    <img src="frontend/src/assets/images/podCreate.png" alt="DetailPage"
  width="700" height="300" >
  <figcaption>파드 생성</figcaption>
</figure>
<figure>
    <img src="frontend/src/assets/images/deplCreate.png" alt="DetailPage"
  width="700" height="300" >
  <figcaption>디플로이먼트 생성</figcaption>
</figure>
<figure>
    <img src="frontend/src/assets/images/serviceCreate.png" alt="DetailPage"
  width="700" height="300" >
  <figcaption>서비스 생성</figcaption>
</figure>



### Replica Scailing Modal
<figure>
    <img src="frontend/src/assets/images/replicaPage.png" alt="replicaPage"
  width="700" height="300" >
  <figcaption>생성/스케일 시 페이지가 3초단위로 reroad 되면서 실시간 업데이트 내용이 반영됨.</figcaption>
</figure>
    

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

- YouTube Link: [https://youtu.be/OW03DGPwbpw?si=2mGGHfPDOLGARDhy](https://youtu.be/OW03DGPwbpw?si=2mGGHfPDOLGARDhy) 
- Project Link: [https://github.com/YEASEUL-JANG/KubeBoard](https://github.com/YEASEUL-JANG/KubeBoard)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


## 💡 알게된 점

- JPA와 QueryDSL을 활용해 객체지향적인 데이터베이스 설계 및 복잡한 검색 조건을 효율적으로 처리하는 방법을 습득.
- Spring Config를 통한 중앙 집중식 구성 관리의 필요성을 이해하고, 서비스 가용성과 유연성을 높이기 위한 설계 방식 적용.
- 메시지 큐 시스템의 작동원리를 알고 Kafka를 통한 비동기 통신을 통해 실시간 로그데이터를 축적하는 기능을 구현.
- Circuit Breaker 패턴을 적용하여 서비스의 장애를 사전에 예방함.
- Kubernetes 의 기본적인 오브젝트 및 리소스 관리를 학습하고 이를 API를 통해 프로그래밍적으로 제어하는 방법을 익힘.
- [Docker를 직접 활용하여 마이크로서비스를 컨테이너화하고, 배포 및 운영 효율성을 높이는 실습을 수행.](https://www.notion.so/_kubeboard-7abafd93a8ff453db81605e037d05eea?pvs=21)

