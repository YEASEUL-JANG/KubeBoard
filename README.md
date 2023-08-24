<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/YEASEUL-JANG/KubeBoard">
    <img src="frontend/src/assets/images/kubeLogo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Kube Board</h3>

  <p align="center">
    ⚙️ k8s client APi를 통해 k8s 상태를 조회할 수 있는 웹페이지
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
💡 ‘KubeBoard’ 는 로컬 쿠버네티스 클러스터 환경을 UI상에서 쉽게 조회할 수 있는 K8S Dashboard 입니다.<br>
현재는 Pod와 Deployment,Service  조회가 가능합니다.
</p>

<img src="frontend/src/assets/images/mainPage.png" alt="MainPage"
  width="500" height="400">
<br>
<p>
💡 조회 내역은 30초 단위로 실시간 업데이트되며, 실제 터미널 조회DATA와 동일합니다. 
</p>
<p>
<figure>
    <img src="frontend/src/assets/images/listPage.png" alt="ListPage"
  width="500" height="200" >
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



### Built With

사용된 frameworks,Tools 및 libraries 입니다. 

* ![SpringBoot][SpringBoot]
* ![Kotlin][Kotlin]
* ![JPA][JPA]
* ![Vue3][Vue3]
* ![JavaScript][JavaScript]
* ![Quartz][Quartz]
* ![Minikube][Minikube]
* ![Docker][Docker]
* ![Bootstrap][Bootstrap]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

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

- [x] k8s fabric client API를 활용한 k8s 데이터 조회 및 Replica Scailing 기능
- [x] Quartz Batch 로 30초 간격 k8s 동기화 
- [x] DB 구조 설계 (JPA)
- [x] QuaryDSL을 활용한 다중 검색기능

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- Pages -->
## Pages

### MainPage
<figure>
    <img src="frontend/src/assets/images/mainPage.png" alt="MainPage"
  width="500" height="400" >
  <figcaption>파드와 디플로이먼트의 현재 실행상태를 차트화, 서비스 데이터를 테이블로 나타냄</figcaption>
</figure>

### ListPage
<img src="frontend/src/assets/images/listPage.png" alt="ListPage1"
  width="500" height="200" >
  <img src="frontend/src/assets/images/deplPage.png" alt="ListPage2"
  width="500" height="200" >
  <img src="frontend/src/assets/images/servicePage.png" alt="ListPage3"
  width="500" height="200" >

### DetailListPage
<figure>
    <img src="frontend/src/assets/images/detailPage.png" alt="DetailPage"
  width="500" height="400" >
  <figcaption>Pod 상세페이지</figcaption>
</figure>

### Replica Scailing Modal
<figure>
    <img src="frontend/src/assets/images/replicaPage.png" alt="replicaPage"
  width="500" height="200" >
  <figcaption>스케일 시 페이지가 3초단위로 reroad 되면서 실시간 업데이트 내용을 반영함</figcaption>
</figure>
    

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

- YouTube : 작업예정
- Project Link: [https://github.com/YEASEUL-JANG/KubeBoard](https://github.com/YEASEUL-JANG/KubeBoard)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
[Vue3]: https://img.shields.io/badge/Vue3-20B2AA?style=for-the-badge
[Kotlin]: https://img.shields.io/badge/Kotlin-orange?style=for-the-badge
[JPA]: https://img.shields.io/badge/JPA-red?style=for-the-badge
[Minikube]: https://img.shields.io/badge/Minikube-blue?style=for-the-badge
[Docker]: https://img.shields.io/badge/Docker-Docker?style=for-the-badge
[JavaScript]: https://img.shields.io/badge/JavaScript-yellow?style=for-the-badge
[Quartz]: https://img.shields.io/badge/Quartz-000000?style=for-the-badge
[Bootstrap]: https://img.shields.io/badge/Bootstrap-purple?style=for-the-badge
[SpringBoot]: https://img.shields.io/badge/SpringBoot-green?style=for-the-badge
