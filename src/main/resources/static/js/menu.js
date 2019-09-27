function logout(){
    localStorage.removeItem('token');
    if (localStorage.getItem('id') !== null){
        localStorage.removeItem('id');
    }
    if (localStorage.getItem('username') !== null){
        localStorage.removeItem('username');
    }
    location.href='../';
}
