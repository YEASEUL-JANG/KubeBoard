<template>
    <div class="intro">
    <img src="../assets/images/logo-icon.png" style="float:left;"/>
    <div class="title">
    
        <TextAnime />
    </div>
    </div>
</template>

<script>
import TextAnime from './TextAnime.vue';


export default {
    name:"Introduction",

    components: {TextAnime}
}
    

</script>

<style scoped>
.intro{
    display: flex;
    justify-content: center;
    padding: 150px;
}
.title{
    text-align: left;
}


</style>