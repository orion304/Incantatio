package interfaces;

import java.util.List;

import org.bukkit.entity.Entity;

public interface CreatedEntities {

	public List<Entity> getEntities();

	public void removeEntity(Entity entity);

}
