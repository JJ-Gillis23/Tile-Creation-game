public class ShootingStarNode
{
   private ShootingStarNode next;
   private ShootingStar star;
   
   public ShootingStarNode(double startingX, double startingY)
   {
      star = new ShootingStar(startingX, startingY);
      next = null;
   }
   
   public ShootingStarNode(ShootingStar star)
   {
      this.star = star;
      next = null;
   }
   
   public ShootingStarNode(double startingX, double startingY, ShootingStarNode next)
   {
      star = new ShootingStar(startingX, startingY);
      this.next = next;
   }
   
   public ShootingStarNode(ShootingStar star, ShootingStarNode next)
   {
      this.star = star;
      this.next = next;
   }
   
   public void setup(double startingX, double startingY)
   {
      star.setup(startingX, startingY);
      next = null;
   }
   
   public void setNext(ShootingStarNode next)
   {
      this.next = next;
   }
   
   public ShootingStarNode getNext()
   {
      return next;
   }
   
   public ShootingStar getStar()
   {
      return star;
   }
}