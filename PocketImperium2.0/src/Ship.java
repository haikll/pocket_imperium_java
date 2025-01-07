package ProjetLatest2;

class Ship {
    private Player owner;
    //private Sector location;

    public Ship(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void moveTo(Sector sector) {
        this.location = sector;
    }
}