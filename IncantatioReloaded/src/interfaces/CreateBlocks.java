package interfaces;

import java.util.List;

import tools.CreatedBlock;

public interface CreateBlocks {

	public List<CreatedBlock> getCreatedBlocks();

	public void removeBlock(CreatedBlock block);

	public void addBlock(CreatedBlock block);

}
