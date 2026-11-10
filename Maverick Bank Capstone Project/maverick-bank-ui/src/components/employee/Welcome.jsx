function Welcome(){
    return(
        <div>
            <h5>Welcome {localStorage.getItem('username')}</h5>
        </div>
    )
}

export default Welcome
