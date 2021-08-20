package xyz.handshot.tconomy

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import xyz.handshot.tconomy.backend.Backend
import xyz.handshot.tconomy.backend.CachedBackend

class JoinLeaveListener(private val backend: Backend) : Listener
{
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	fun onPreLogin(event: AsyncPlayerPreLoginEvent)
	{
		if (backend is CachedBackend)
		{
			backend.load(event.uniqueId)
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	fun onQuit(event: PlayerQuitEvent)
	{
		if (backend is CachedBackend)
		{
			val account = backend.load(event.player.uniqueId)
			if (account != null)
			{
				backend.save(account)
				backend.invalidate(event.player.uniqueId)
			}
		}
	}
}