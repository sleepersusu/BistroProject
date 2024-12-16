    <template>
    
    <button @click.prevent="addComment">新增評論</button>
    <CommentPost v-if="showCommentPost"></CommentPost>
    
    </template>   
    
    <script >
    import CommentPost from '@/components/CommentPostForm.vue';

    
    
    
    export default {
    components:{
        CommentPost,
    },
    
    data() {
        return {
            showCommentPost: false

        }
    },
    methods: {
        addComment() { 
            this.showCommentPost = true; 
        }
    
    
    }
}
    </script>
    
    <style scoped>

    
    </style>