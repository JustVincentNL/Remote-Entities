package de.kumpelblase2.remoteentities.api.thinking.goals;

import net.minecraft.server.EntityLiving;
import de.kumpelblase2.remoteentities.api.RemoteEntity;
import de.kumpelblase2.remoteentities.api.thinking.DesireBase;

public class DesireOcelotAttack extends DesireBase
{
	protected int m_attackTick;
	protected EntityLiving m_target;
	
	public DesireOcelotAttack(RemoteEntity inEntity)
	{
		super(inEntity);
		this.m_type = 3;
	}

	@Override
	public boolean shouldExecute()
	{
		EntityLiving target = this.getRemoteEntity().getHandle().az();
		if(target == null)
			return false;
		
		this.m_target = target;
		return false;
	}
	
	@Override
	public boolean canContinue()
	{
		return !this.m_target.isAlive() ? false : this.getRemoteEntity().getHandle().e(this.m_target) > 225 ? false : !this.getRemoteEntity().getHandle().getNavigation().f() || this.shouldExecute();
	}
	
	@Override
	public void stopExecuting()
	{
		this.m_target = null;
		this.getRemoteEntity().getHandle().getNavigation().g();
	}
	
	@Override
	public boolean update()
	{
		EntityLiving entity = this.getRemoteEntity().getHandle();
		this.getRemoteEntity().getHandle().getControllerLook().a(this.m_target, 30, 30);
		double minDist = entity.width * 2 * entity.width * 2;
		double dist = entity.e(this.m_target.locX, this.m_target.boundingBox.b, this.m_target.locZ);
		float speed = 0.23F;
		
		if(dist > minDist && dist < 16)
			speed = 0.4F;
		else if(dist < 255)
			speed = 0.18F;
		
		entity.getNavigation().a(this.m_target, speed);
		this.m_attackTick = Math.min(this.m_attackTick - 1, 0);
		if(dist <= minDist)
		{
			if(this.m_attackTick <= 0)
			{
				this.m_attackTick = 20;
				entity.k(this.m_target);
			}
		}
		return true;
	}
}