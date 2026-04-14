import java.util.*;

public class ShootingStarLinkedList
{
   private ShootingStarNode head;
   private ArrayList<ShootingStarNode> recycleQueue;
   
   //private static ShootingStarLinkedList instance;

   public ShootingStarLinkedList()
   {
      head = null;
      recycleQueue = new ArrayList<ShootingStarNode>();
      //System.out.println("Recycle Created!");
      //System.exit(0);
   }
   
   /*
   public static ShootingStarLinkedList getInstance()
   {
      if(instance == null)
      {
         instance = new ShootingStarLinkedList();
      }
      return instance;
   }
   */
   
   /*
   public void insert(int index, ShootingStarNode newNode)
   {
      if(index == 0)
      {
         if(head == null)
         {
            head = newNode;
         }
         else
         {
            newNode.setNext(head);
            head = newNode;
         }
      }
      else
   }
   */
   
   //private void insert()
   
   public ShootingStarNode getHead()
   {
      return head;
   }
   
   /*
   public void add(ShootingStarNode newNode)
   {
      if(head == null)
      {
         head = newNode;
      }
      else
      {
         newNode.setNext(head);
         head = newNode;
      }
   }
   */
   
   public void add(double startingX, double startingY)
   {
      ShootingStarNode newNode;
      if(recycleQueue.isEmpty())
      {
         newNode = new ShootingStarNode(startingX, startingY);
         //System.out.println("recycle queue empty");
      }
      else
      {
         newNode = recycleQueue.remove(0);
         //will set up this node to be essentially a brand new node
         newNode.setup(startingX, startingY);
      }
      if(head == null)
      {
         head = newNode;
      }
      else
      {
         newNode.setNext(head);
         head = newNode;
      }
   }
   
   public void remove(ShootingStarNode previousNode, ShootingStarNode nodeToRemove)
   {
      if(previousNode == null)
      {
         head = nodeToRemove.getNext();
      }
      else
      {
         previousNode.setNext(nodeToRemove.getNext());
         recycleQueue.add(nodeToRemove);
      }
   }
   
   public boolean isEmpty()
   {
      return head == null;
   }
   
   public void clear()
   {
      head = null;
   }
   
   
}